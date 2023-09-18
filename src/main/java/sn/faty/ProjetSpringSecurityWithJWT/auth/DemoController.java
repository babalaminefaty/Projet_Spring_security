package sn.faty.ProjetSpringSecurityWithJWT.auth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DemoController {

    Logger logger = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/faty")
    public ResponseEntity<String> sayHello(){
         var x= 10 ;

         logger.debug("welcome faty:{}" , x);

        return  ResponseEntity.ok("helllo word");

    }

}
