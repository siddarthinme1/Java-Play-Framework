package models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data @ApiModel
public class UserAccount {
    @ApiModelProperty(name = "userName", example = "userName", required = true)
    private String userName;
    @ApiModelProperty(name = "password", example = "password@123", required = true)
    private String password;
    @ApiModelProperty(name = "firstName", example = "firstName", required = true)
    private String firstName;
    @ApiModelProperty(name = "lastName", example = "lastName", required = true)
    private String lastName;
}