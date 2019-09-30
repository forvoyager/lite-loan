import com.etl.base.common.util.FeeCalcUtils;
import com.etl.base.common.util.JsonUtils;
import org.junit.Test;

/**
 * @Author: forvoyager@outlook.com
 * @Time: 2019-09-30 15:19
 * @Description:
 */
public class FeeCalcTest {

  /**
   * 等额本息还款
   * @throws Exception
   */
  @Test
  public void test_averageCapitalPlusInterest() throws Exception{
    // 借款100元，年化利率10%，借款期限10个月
    System.out.println("等额本息还款:\n"+JsonUtils.parseJson(FeeCalcUtils.averageCapitalPlusInterest(100, 0.1, 10)));;
  }

  /**
   * 等额本金还款
   * @throws Exception
   */
  @Test
  public void test_averageCapital() throws Exception{
    // 借款100元，年化利率10%，借款期限10个月
    System.out.println("等额本金还款:\n"+JsonUtils.parseJson(FeeCalcUtils.averageCapital(100, 0.1, 10)));;
  }

  /**
   * 每月付息到期还本
   * @throws Exception
   */
  @Test
  public void test_averageInterest() throws Exception{
    // 借款100元，年化利率10%，借款期限12个月
    System.out.println("每月付息到期还本:\n"+JsonUtils.parseJson(FeeCalcUtils.averageInterest(100, 0.1, 12)));;
  }
  
}
