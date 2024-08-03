package tadodemo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private TadoApiDemo tadoApiDemo;

    AppStartupRunner(final TadoApiDemo tadoApiDemo) {
        this.tadoApiDemo = tadoApiDemo;

    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        tadoApiDemo.doIt();
    }
}
