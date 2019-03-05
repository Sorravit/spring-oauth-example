package sorravit.example.spring.controller

import org.springframework.core.SpringVersion
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ExampleController {

    @ResponseBody
    @RequestMapping("/")
    fun testHello(): String {
        System.out.println("version: " + SpringVersion.getVersion())
        return "Hello from Spring" + SpringVersion.getVersion()
    }
    @ResponseBody
    @RequestMapping("/fail")
    fun testFail(): String {
        throw Exception("WTF")
    }
}
