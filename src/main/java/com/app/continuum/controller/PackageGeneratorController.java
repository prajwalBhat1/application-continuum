package com.app.continuum.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.continuum.model.Root;
import com.app.continuum.service.PackageGeneratorService;

@RestController
public class PackageGeneratorController {

	@Autowired
	private PackageGeneratorService generatorService;
	
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	@RequestMapping(value = { "/application/continuum/api/v1/generate" }, method = {
			RequestMethod.POST }, produces = "application/zip")
	public ResponseEntity<?> generatePackage(HttpServletRequest httpServletRequest, @RequestBody Root project) {

		generatorService.generatePom(project.getProject());
		
		
		return null;

	}

}
