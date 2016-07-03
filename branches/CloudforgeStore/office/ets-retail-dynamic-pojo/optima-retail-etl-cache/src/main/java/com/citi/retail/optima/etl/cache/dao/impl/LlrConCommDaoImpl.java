package com.citi.retail.optima.etl.cache.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

import ch.qos.logback.classic.Logger;

import com.citi.retail.optima.etl.cache.dao.CacheLoaderDao;
import com.citi.retail.optima.etl.cache.resultset.setter.LlrConCommSetExtractor;
import com.citi.retail.optima.etl.common.ServiceName;
import com.citi.retail.optima.etl.common.exception.ExceptionType;
import com.citi.retail.optima.etl.common.exception.OptimaRetailApplicationException;
import com.citi.retail.optima.etl.common.exception.util.OptimaRetailExceptionUtil;
import com.citi.retail.optima.etl.common.model.cache.LlrConCommDomain;

/**
 * @author sr67841 This is the DAO class that loads the FICO from reference
 *         tables
 * 
 */
public class LlrConCommDaoImpl implements CacheLoaderDao<LlrConCommDomain>,
		InitializingBean {

	private final Logger LOGGER = (Logger) LoggerFactory
			.getLogger(LlrConCommDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	private List<LlrConCommDomain> llrTformRefList;

	private String sql;

	public List<LlrConCommDomain> getCacheData()
			throws OptimaRetailApplicationException {

		llrTformRefList = new ArrayList<LlrConCommDomain>();
		try {
			llrTformRefList = jdbcTemplate.query(sql,
					new LlrConCommSetExtractor());

		} catch (Exception e) {
			LOGGER.error(" Exception occured while loading FICO " + e);
			throw new OptimaRetailApplicationException(null, null, null,
					ServiceName.CACHE, ExceptionType.ERROR,
					"Exception occured while loading FICO", e.getMessage(),
					OptimaRetailExceptionUtil.getStackTrace(e));
		}
		return llrTformRefList;
	}

	@Override
	public List<LlrConCommDomain> getCacheData(Object... args) throws Exception {
		return llrTformRefList;
	}

	/**
	 * 
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @param sQL
	 *            the sQL to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		getCacheData();

	}

}