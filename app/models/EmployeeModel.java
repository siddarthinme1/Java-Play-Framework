package models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data @ApiModel
public class EmployeeModel {

    @ApiModelProperty(name = "gender", example = "Male", required = true)
    private String gender;
    @ApiModelProperty(name = "firstName", example = "Siddarood", required = true)
    private String firstName;
    @ApiModelProperty(name = "lastName", example = "Karachuri", required = true)
    private String lastName;
    @ApiModelProperty(name = "phone", example = "8123001085", required = true)
    private String phone;
    @ApiModelProperty(name = "mail", example = "siddarthinme@gmail.com", required = true)
    private String mail;
    @ApiModelProperty(name = "birthday", example = "2023-07-20T08:59:10.105Z", required = true)
    private String birthday;
    @ApiModelProperty(name = "bloodId", example = "1", required = true)
    private Integer bloodId;
    @ApiModelProperty(name = "streetAddress", example = "Akalwadi")
    private String streetAddress;
    @ApiModelProperty(name = "streetAddress2", example = "Hulagabali")
    private String streetAddress2;
    @ApiModelProperty(name = "city", example = "Athani")
    private String city;
    @ApiModelProperty(name = "state", example = "Karnataka")
    private String state;
    @ApiModelProperty(name = "country", example = "India")
    private String country;
    @ApiModelProperty(name = "zipcode", example = "560016")
    private String zipcode;
    @ApiModelProperty(name = "genderx", example = "Male")
    private String genderx;
    @ApiModelProperty(name = "firstNamex", example = "Achyut")
    private String firstNamex;
    @ApiModelProperty(name = "lastNamex", example = "Karachuri")
    private String lastNamex;
    @ApiModelProperty(name = "relationxId", example = "2")
    private Integer relationxId;
    @ApiModelProperty(name = "phonex", example = "8123001085")
    private String phonex;
    @ApiModelProperty(name = "streetAddressx", example = "Akalawadi")
    private String streetAddressx;
    @ApiModelProperty(name = "streetAddress2x", example = "Hulagabali")
    private String streetAddress2x;
    @ApiModelProperty(name = "cityx", example = "Athani")
    private String cityx;
    @ApiModelProperty(name = "statex", example = "Karnataka")
    private String statex;
    @ApiModelProperty(name = "countryx", example = "India")
    private String countryx;
    @ApiModelProperty(name = "zipcodex", example = "560016")
    private String zipcodex;
}

