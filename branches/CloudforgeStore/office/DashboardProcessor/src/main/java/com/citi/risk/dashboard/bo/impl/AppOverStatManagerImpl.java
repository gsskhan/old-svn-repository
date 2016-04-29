package com.citi.risk.dashboard.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.citi.risk.dashboard.bo.AppOverStatManager;
import com.citi.risk.dashboard.dao.AppOverallStatusDao;
import com.citi.risk.dashboard.dao.AppStatusDao;
import com.citi.risk.dashboard.entity.AppOverallStatus;
import com.citi.risk.dashboard.entity.Apps;
import com.citi.risk.dashboard.exception.DashboardException;
import com.citi.risk.dashboard.util.ColorPicker;
import com.citi.risk.dashboard.util.ConstantsUtil;

/**
 * @author gk85985 This class contain method to implement business logic, for
 *         maintaining data in table -
 *         "CLOUDAPP"."ETS_DSHBD_APP_OVERALL_STATUS".
 */
@Service("appOverStatManager")
public class AppOverStatManagerImpl implements AppOverStatManager {

    private Logger log = Logger.getLogger(AppOverStatManagerImpl.class);
    @Autowired
    private AppStatusDao appStatusDao;
    @Autowired
    private AppOverallStatusDao appOverStatusDao;

    public boolean addToAppOverStat(Apps app, Date msgDateTime)
            throws DashboardException {
        log.info("Begin adding record to AppOverallStatus entity (ETS_DSHBD_APP_OVERALL_STATUS).");
        if (app != null) {
            // Get list of latest id(s)
            String listOfIds = csvlistOfLatestId(app.getAppsId());
            log.info("List of latest records =" + listOfIds);
            // Find list of colors for above id(s)
            List<String> listOfColors = getListColorCode(listOfIds);
            log.info("List of colors to choose from = " + listOfColors);
            // Choose the max priority color
            String color = new ColorPicker().getMaxColor(listOfColors);
            log.info("Calculated final color = " + color);
            appOverStatusDao.saveAppOverallStatus(new AppOverallStatus(app,
                    color, msgDateTime, ConstantsUtil.UPDATE_USER_NAME_DEV
                            .getConstants()));
            log.info("Finished adding record to AppOverallStatus entity (ETS_DSHBD_APP_OVERALL_STATUS).");
            return true;
        } else {
            log.error("Apps object is null.  No record saved for AppOverallStatus entity (ETS_DSHBD_APP_OVERALL_STATUS).");
            return false;
        }
    }

    /*
     * To Find the list of latest id for KPI_CATEGORY_NAME belonging to a
     * Application Id.
     */
    @SuppressWarnings("unchecked")
    public String csvlistOfLatestId(long appsId) throws DashboardException {
        List<Object> tmplist = (List<Object>) appStatusDao
                .listMaxIdByKpiCatName(appsId);
        List<Long> maxIdList = new ArrayList<Long>();
        Iterator<Object> itr = tmplist.iterator();
        while (itr.hasNext()) {
            Object[] obj = (Object[]) itr.next();
            maxIdList.add((Long) obj[1]);
            log.debug(obj[0] + " : " + obj[1]);
        }
        if (maxIdList.size() > 0) {
            return StringUtils.collectionToCommaDelimitedString(maxIdList);
        }
        return null;
    }

    /*
     * To get list of distinct colors for the input csv list of latest
     * appStatusId(s).
     */
    @SuppressWarnings("unchecked")
    public List<String> getListColorCode(String csvListOfIds)
            throws DashboardException {
        if (csvListOfIds != null && csvListOfIds.length() > 0) {
            return (List<String>) appStatusDao.listOfColorCodes(csvListOfIds);
        }
        return null;
    }

    // getter-setter methods
    public AppStatusDao getAppStatusDao() {
        return appStatusDao;
    }

    public void setAppStatusDao(AppStatusDao appStatusDao) {
        this.appStatusDao = appStatusDao;
    }

    public AppOverallStatusDao getAppOverStatusDao() {
        return appOverStatusDao;
    }

    public void setAppOverStatusDao(AppOverallStatusDao appOverStatusDao) {
        this.appOverStatusDao = appOverStatusDao;
    }
}
