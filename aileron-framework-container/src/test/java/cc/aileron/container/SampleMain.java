/**
 *
 */
package cc.aileron.container;

import cc.aileron.container.scope.Singleton;

/**
 * @author aileron
 */
public class SampleMain
{
    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        final Injector injector = IOC.createInjector(new Module()
        {
            @Override
            public void configure(final Binder binder)
            {
                binder.bind(SampleLogic3.class)
                        .to(SampleLogic3Impl.class)
                        .in(Singleton.class);
            }
        });
        final SampleMain main = injector.getInstance(SampleMain.class);
        main.run();
    }

    /**
     * run
     */
    public void run()
    {
        System.out.println(0 == sampleLogic.count());
        System.out.println(1 == sampleLogic.count());
        System.out.println(2 == sampleLogic.count());

        System.out.println(3 == sampleLogicProvider.get().count());
        System.out.println(4 == sampleLogicProvider.get().count());
        System.out.println(5 == sampleLogicProvider.get().count());
        System.out.println(6 == sampleLogicProvider.get().count());

        System.out.println(7 == sampleLogic.count());
        System.out.println(8 == sampleLogic.count());

        System.out.println(0 == sampleLogic2.count());
        System.out.println(1 == sampleLogic2.count());
        System.out.println(2 == sampleLogic2.count());
        System.out.println(3 == sampleLogic2.count());
        System.out.println(4 == sampleLogic2.count());

        System.out.println(0 == sampleLogic3p.get().count());
        System.out.println(1 == sampleLogic3p.get().count());
        System.out.println(2 == sampleLogic3p.get().count());
        System.out.println(3 == sampleLogic3p.get().count());
    }

    /**
     * @param sampleLogic
     * @param sampleLogicProvider
     * @param sampleLogic2
     * @param sampleLogic3p
     */
    @Inject
    public SampleMain(final SampleLogic sampleLogic,
            final Provider<SampleLogic> sampleLogicProvider,
            final SampleLogic2 sampleLogic2,
            final Provider<SampleLogic3> sampleLogic3p)
    {
        this.sampleLogic = sampleLogic;
        this.sampleLogicProvider = sampleLogicProvider;
        this.sampleLogic2 = sampleLogic2;
        this.sampleLogic3p = sampleLogic3p;
    }

    private final SampleLogic sampleLogic;
    private final SampleLogic2 sampleLogic2;
    private final Provider<SampleLogic3> sampleLogic3p;
    private final Provider<SampleLogic> sampleLogicProvider;
}
