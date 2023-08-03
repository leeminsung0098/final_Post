package javaproject.project.CreateForm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessCreateForm {
    @NotEmpty(message = "사업자번호는 필수항목입니다.")
    @NotBlank(message = "사업자번호가 비어있습니다.")
    private String businessman_num;


    @NotEmpty(message = "사업자사이트는 필수항목입니다.")
    @NotBlank(message = "사업자사이트가 비어있습니다.")
    private String businessman_site;

    @NotEmpty(message = "사업자주소는 필수항목입니다.")
    @NotBlank(message = "사업자주소가 비어있습니다.")
    private String businessman_address;
}
