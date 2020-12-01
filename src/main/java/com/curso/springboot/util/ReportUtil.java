package com.curso.springboot.util;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	public byte[] gerarRelatorio(List listDados, String relatorio, ServletContext servletContext) throws Exception {
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDados);
		
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + relatorio + ".jasper";
		
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashedMap<>(), jrbcds);
		
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
}
