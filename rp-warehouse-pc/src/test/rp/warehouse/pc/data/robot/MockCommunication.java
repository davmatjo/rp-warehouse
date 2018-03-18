package rp.warehouse.pc.data.robot;

import static org.mockito.Mockito.*;

public class MockCommunication  {

    @Mock
    MyDatabase databaseMock; 

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 

    @Test
    public void testQuery()  {
        ClassToTest t  = new ClassToTest(databaseMock); 
        boolean check = t.query("* from t"); 
        assertTrue(check); 
        verify(databaseMock).query("* from t"); 
    }
}
