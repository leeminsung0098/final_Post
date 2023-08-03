package javaproject.project.Utill.summerNote;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectPath = System.getProperty("user.dir");
        String path = "/src/main/resources/static/image/SummerNote/";

        registry.addResourceHandler("/summernoteImage/**")
                .addResourceLocations("file:///"+ projectPath +path);
//                .addResourceLocations("file:///C:/summernote_image/");


    }
}