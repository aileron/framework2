/**
 *
 */
package cc.aileron.wsgi.compile;

import java.util.Locale;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

/**
 * @author aileron
 */
public class WsgiCompiler
{
    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        final ICompilationUnit[] compilationUnits = new ICompilationUnit[] { new WsgiCompilerUnit() };
        final IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();
        final CompilerOptions settings = new CompilerOptions();
        final IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());
        final INameEnvironment nameEnvironment = new INameEnvironment()
        {
            @Override
            public void cleanup()
            {
            }

            @Override
            public NameEnvironmentAnswer findType(final char[] arg0,
                    final char[][] arg1)
            {
                return null;
            }

            @Override
            public NameEnvironmentAnswer findType(final char[][] arg0)
            {
                return null;
            }

            @Override
            public boolean isPackage(final char[][] arg0, final char[] arg1)
            {
                return false;
            }
        };
        final ICompilerRequestor compilerRequestor = new ICompilerRequestor()
        {
            @Override
            public void acceptResult(final CompilationResult compilationResult)
            {

            }
        };

        /**
         * The JDT compiler
         */
        final Compiler jdtCompiler = new Compiler(nameEnvironment,
                policy,
                settings,
                compilerRequestor,
                problemFactory)
        {
            @Override
            protected void handleInternalException(final Throwable e,
                    final CompilationUnitDeclaration ud,
                    final CompilationResult result)
            {
            }
        };
        jdtCompiler.compile(compilationUnits);
    }

    /**
     * @param path
     */
    public WsgiCompiler(final String path)
    {
        this.path = path;
    }

    private final String path;

}
