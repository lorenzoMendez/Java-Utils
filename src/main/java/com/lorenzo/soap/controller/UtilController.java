package com.lorenzo.soap.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lorenzo.soap.commons.BaseResponse;

@RestController
@CrossOrigin( origins = "*" )
@RequestMapping( "/util" )
public class UtilController extends BaseResponse {
}
