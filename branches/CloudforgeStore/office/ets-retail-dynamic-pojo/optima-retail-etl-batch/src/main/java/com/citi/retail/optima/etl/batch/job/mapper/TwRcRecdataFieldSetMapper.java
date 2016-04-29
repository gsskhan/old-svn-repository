package com.citi.retail.optima.etl.batch.job.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.citi.retail.optima.etl.common.log.LoggingMarker;
import com.citi.retail.optima.etl.common.model.TwRcRecdataRecord;

public class TwRcRecdataFieldSetMapper extends AbstractFieldSetMapper implements FieldSetMapper{
	public TwRcRecdataRecord mapFieldSet(FieldSet fieldSet) throws BindException {
		TwRcRecdataRecord record = new TwRcRecdataRecord();
		record.setCrdAcctNbr(fieldSet.readString("crdAcctNbr"));
		//record.setCrdNbr(fieldSet.readString("accNumber"));
		record.setNewrcP(readDouble(fieldSet,"newrcP"));
		/**For Logging
		 * 
		 */
		if(getExceptionList().size() > 0) {
			KPILOGGER.error(LoggingMarker.KPI.getMarker(), "ERROR Record: "+ record.toString());
			for(Exception e: getExceptionList()) {
				KPILOGGER.error(LoggingMarker.KPI.getMarker(), "ERROR DETAILS: "+e.getMessage());
			}
			getExceptionList().clear();
			KPILOGGER.error(LoggingMarker.KPI.getMarker(), "\n");
		}
		return record;
	}
}