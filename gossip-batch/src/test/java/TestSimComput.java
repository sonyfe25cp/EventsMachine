

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import cn.omartech.gossip.dict.MatrixItem;
import cn.omartech.gossip.dict.VectorPrepare;


public class TestSimComput extends TestCase{

    @Test
    public void testDot(){
        String t1 = "我爱北京天安门，天安门上太阳升，科技公司";
        String t2 = "北京天安门上有红太阳,公司不错";

        String l1 = VectorPrepare.parseVector(t1);
        System.out.println(l1);
        String l2 = VectorPrepare.parseVector(t2);
        System.out.println(l2);
        List<MatrixItem> m1 = MatrixItem.parseFromLines(l1);
        MatrixItem.debug(m1);
        List<MatrixItem> m2 = MatrixItem.parseFromLines(l2);
        MatrixItem.debug(m2);
        double res = MatrixItem.dot(m1, m2);
        System.out.println(res);
        assertEquals(5.0, res);
    }
    
}
