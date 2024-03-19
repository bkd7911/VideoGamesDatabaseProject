package model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.project.api.projectapi.model.Template;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("model")
public class TemplateTest {

    @Test
    public void test_getTemid() {
        int intended_temid = 11;
        Template newTemplate  = new Template(intended_temid,"Name","Message",false);
        int actual_temid = newTemplate.getTemid();
        assertEquals(intended_temid, actual_temid);
    }

    @Test
    public void test_getTname() {
        String intended_tname = "Testing The Tename 1234";
        Template newTemplate  = new Template(0, intended_tname,"",false);
        String actual_tname = newTemplate.getTname();
        assertEquals(intended_tname, actual_tname);
    }

    @Test
    public void test_getTmess() {
        String intended_tmess = "Testing The Tmess: Good Morning!";
        Template newTemplate  = new Template(0, "",intended_tmess,false);
        String actual_tmess = newTemplate.getTmess();
        assertEquals(intended_tmess, actual_tmess);
    }

    @Test
    public void test_getTbool() {
        
        Template trueTemplate  = new Template(0, null, null, true);
        Template falseTemplate  = new Template(0, null, null, false);
        assertEquals(true, trueTemplate.getTbool());
        assertEquals(false, falseTemplate.getTbool()); 
    }
    
}
