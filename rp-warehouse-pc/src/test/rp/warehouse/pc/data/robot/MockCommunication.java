//package rp.warehouse.pc.data.robot;
//
//import static org.mockito.Mockito.*;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoRule;
//import org.junit.jupiter.api.Assertions;
//
//public class MockCommunication  {
//
//    @Mock
//    MyDatabase databaseMock; 
//
//    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
//
//    @Test
//    public void testQuery()  {
//        ClassToTest t  = new ClassToTest(databaseMock); 
//        boolean check = t.query("* from t"); 
//        Assertions.assertTrue(check); 
//        verify(databaseMock).query("* from t"); 
//    }
//}
