package ai.preferred.cornac.util;

import org.apache.commons.math3.stat.inference.TTest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TtestUtil {

    private final TTest TTEST = new TTest();

    public Double ttest(double[] a, double[] b) {
        return TTEST.t(a, b);
    }

    public Double pValue(double[] a, double[] b) {
        return TTEST.tTest(a, b);
    }

    public Double pairedTTest(double[] a, double[] b) {
        return TTEST.pairedT(a, b);
    }

    public Double pairedPvalue(double[] a, double[] b) {
        return TTEST.pairedTTest(a, b);
    }
}
