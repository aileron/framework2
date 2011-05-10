/**
 *
 */
package cc.aileron.wsgi.mobile;

import java.io.IOException;
import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import cc.aileron.template.Template;
import cc.aileron.template.TemplateCategory;
import cc.aileron.template.TemplateCompiler;
import cc.aileron.template.TemplateCompilerMap;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class MobileHtmlTemplateManagerImpl implements MobileHtmlTemplateManager
{
    @Override
    public EnumMap<MobileCarrier, Template> get(final String html,
            final CssSelectorProperties css)
            throws SAXException, IOException, TransformerException,
            TemplateSyntaxEexception, ParserMethodNotFoundException
    {
        final EnumMap<MobileCarrier, Template> map = new EnumMap<MobileCarrier, Template>(MobileCarrier.class);
        for (final MobileCarrier carrier : MobileCarrier.values())
        {
            /*
             * mobile 以外の場合は、そのまま
             */
            if (!carrier.isMobile())
            {
                map.put(carrier, compiler.compile(html));
                continue;
            }

            String convertHtml = new String(html);

            /*
             * doctype 部分までを削除
             */
            final MobileCarrier htmlcarrier;
            final Matcher matcher = extractDoctype.matcher(convertHtml);
            if (matcher.find())
            {
                htmlcarrier = MobileCarrier.parseDocType(matcher.group(1));
                convertHtml = convertHtml.substring(matcher.end());
            }
            else
            {
                htmlcarrier = MobileCarrier.OTHER;
            }

            /*
             * htmlのキャリアと、表示キャリアが同一の場合
             */
            if (carrier == htmlcarrier)
            {
                map.put(carrier, compiler.compile(html));
                continue;
            }

            /*
             * 絵文字の置き換え
             */
            logger.debug("emojiConvertor.convert({},{},?}",
                    htmlcarrier,
                    carrier);
            convertHtml = emojiConvertor.convert(htmlcarrier,
                    carrier,
                    convertHtml);

            /*
             * css のインライン化
             */
            if (carrier.enableCssInline())
            {
                convertHtml = cssInlineConvertor.convert(carrier,
                        convertHtml,
                        css);
            }
            else
            {
                /*
                 * xml宣言 + doctype を先頭に付与
                 */
                convertHtml = carrier.xmlDeclaration() + carrier.doctype()
                        + convertHtml;
            }

            /*
             * 変換後テンプレートの格納
             */
            map.put(carrier, compiler.compile(convertHtml));
        }
        return map;
    }

    /**
     * @param compilerMap
     * @param cssInlineConvertor
     * @param emojiConvertor
     */
    @Inject
    public MobileHtmlTemplateManagerImpl(final TemplateCompilerMap compilerMap,
            final MobileHtmlCssInlineConvertor cssInlineConvertor,
            final MobileHtmlEmojiConvertor emojiConvertor)
    {
        this.emojiConvertor = emojiConvertor;
        this.compiler = compilerMap.get(TemplateCategory.XML);
        this.cssInlineConvertor = cssInlineConvertor;
    }

    private final TemplateCompiler compiler;

    private final MobileHtmlCssInlineConvertor cssInlineConvertor;
    private final MobileHtmlEmojiConvertor emojiConvertor;
    private final Pattern extractDoctype = Pattern.compile("(<!DOCTYPE.+?>)",
            Pattern.CASE_INSENSITIVE);
    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
}
