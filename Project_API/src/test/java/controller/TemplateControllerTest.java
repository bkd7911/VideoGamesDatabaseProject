package controller;
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.project.api.projectapi.model.Template;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.api.projectapi.controller.TemplateController;
import com.project.api.projectapi.persistance.Template.TemplateDAO;
///////////////////////////////////////////////////////////////////////////////////////////////////////

@Tag("controller")
public class TemplateControllerTest {
    private Template[] tempArray;
    private TemplateDAO mockDAO  = mock(TemplateDAO.class);
    private TemplateController mockController = new TemplateController(mockDAO);

    @BeforeEach
    public void setuploginController() {
        tempArray = new Template[3];

        tempArray[0] = new Template(1, "1st N", "1st M", true);
        tempArray[1] = new Template(2, "2nd N", "2nd M", false);
        tempArray[2] = new Template(3, "3rd N", "3rd M", true); 
    }

    @Test
    public void test_GET_All() throws IOException{

        when((mockDAO).getTemplates(-1)).thenReturn(tempArray);
        ResponseEntity<Template[]> response = mockController.getAllTemps();

        assertEquals(tempArray ,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());

        mockController.testingCatch = true;
        response = mockController.getAllTemps();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()); 
    }
    @Test
    public void test_GET_One_URL() throws IOException{
        Template[] retVal = {tempArray[0]};
        ResponseEntity<Template[]> response;
        when((mockDAO).getTemplates(0)).thenReturn(retVal);
        when((mockDAO).getTemplates(1)).thenReturn(null);

        response = mockController.getTLFromPaths(0);
        assertEquals(retVal ,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());

        response = mockController.getTLFromPaths(1);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        mockController.testingCatch = true;
        response = mockController.getTLFromPaths(1);;
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }   
    @Test
    public void test_GET_One_Path() throws IOException{
        Template[] retVal = {tempArray[0]};
        when((mockDAO).getTemplates(0)).thenReturn(retVal);
        when((mockDAO).getTemplates(1)).thenReturn(null);

        ResponseEntity<Template[]> response = mockController.getTLFromParam(0);
        assertEquals(retVal ,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());

        response = mockController.getTLFromParam(1);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode()); 

        mockController.testingCatch = true;
        response = mockController.getTLFromParam(1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    } 
    @Test
    public void test_POST() throws IOException{
        ResponseEntity<Boolean> response;
        when((mockDAO).createTemplate(null, null, true)).thenReturn(true);
        when((mockDAO).createTemplate(null, null, false)).thenReturn(false); 

        response = mockController.createTemplate(null, null, true);
        assertEquals(true, response.getBody()); 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = mockController.createTemplate(null, null, false);
        assertEquals(false, response.getBody()); 
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        mockController.testingCatch = true;
        response = mockController.createTemplate(null, null, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    public void test_PUT() throws IOException{
        ResponseEntity<Boolean> response;
        when((mockDAO).updateTemplate(0,null, null, null)).thenReturn(true);
        when((mockDAO).updateTemplate(1, null, null, null)).thenReturn(false);  
        
        response = mockController.updateTemplate(0,null, null, null);
        assertEquals(true, response.getBody()); 
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = mockController.updateTemplate(1, null, null, null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        mockController.testingCatch = true;
        response = mockController.updateTemplate(0, null, null, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    public void test_DELETE() throws IOException{
        ResponseEntity<Boolean> response;
        when((mockDAO).deleteTemplate(0)).thenReturn(true);
        when((mockDAO).deleteTemplate(1)).thenReturn(false); 

        response = mockController.deleteTemplate(0);
        assertEquals(true, response.getBody()); 
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = mockController.deleteTemplate(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        mockController.testingCatch = true;
        response = mockController.deleteTemplate(0);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
