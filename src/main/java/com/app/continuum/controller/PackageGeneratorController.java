package com.app.continuum.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping(value = { "/application/continuum/api/v1/generate" }, method = {
			RequestMethod.POST }, produces = "application/zip")
	public void generatePackage(HttpServletRequest httpServletRequest, @RequestBody Root project,
			HttpServletResponse response) {
		// setting headers
		response.setContentType("application/zip");
		// response.setHeader("Content-type", "application-download");
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition",
				"attachment;" + "filename=\\" + project.getProject().getArtifactId() + ".zip");
		OutputStream outStream;
		try {
			outStream = response.getOutputStream();
			outStream.write(generatorService.generatePom(project.getProject()));
			outStream.close();
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
