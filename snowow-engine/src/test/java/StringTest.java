import com.vau.snowow.engine.utils.StringUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StringTest {
    @Test
    public void testStringUtil() {
        boolean test1 = StringUtil.isFormValue("@{param.sss}");
        org.junit.Assert.assertTrue(test1);
        boolean test2 = StringUtil.isFormValue("@{params.ss");
        org.junit.Assert.assertFalse(test2);
        boolean test3 = StringUtil.isFormValue("@params.ss}");
        org.junit.Assert.assertFalse(test3);
        boolean test4 = StringUtil.isFormValue("{params.ss}");
        org.junit.Assert.assertFalse(test4);
        boolean test5 = StringUtil.isFormValue("params.ss}");
        org.junit.Assert.assertFalse(test5);
        boolean test6 = StringUtil.isFormValue("params.ss");
        org.junit.Assert.assertFalse(test6);
    }

    @Test
    public void testSubString() {
        String name = "@{params.name}";
        Assert.assertEquals("params.name", name.substring(2, name.length() - 1));
    }
}
