package models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data @ApiModel
public class UserAccount {
    @ApiModelProperty(name = "userName", example = "Male", required = true)
    private String userName;
    @ApiModelProperty(name = "password", example = "Male", required = true)
    private String password;
    @ApiModelProperty(name = "firstName", example = "Male", required = true)
    private String firstName;
    @ApiModelProperty(name = "lastName", example = "Male", required = true)
    private String lastName;
}