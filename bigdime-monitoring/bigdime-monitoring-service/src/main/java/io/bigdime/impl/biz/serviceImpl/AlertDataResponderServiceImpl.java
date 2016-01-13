/**
 * Copyright (C) 2015 Stubhub.
 */
package io.bigdime.impl.biz.serviceImpl;

import static io.bigdime.impl.biz.constants.ApplicationConstants.SOURCE_TYPE;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.bigdime.alert.AlertException;
import io.bigdime.alert.Logger;
import io.bigdime.alert.LoggerFactory;
import io.bigdime.impl.biz.dao.AlertListDao;
import io.bigdime.impl.biz.exception.AuthorizationException;
import io.bigdime.impl.biz.service.AlertDataResponderService;

/**
 * Class AlertDataResponderServiceImpl is an implementation class for
 * bigdime-monitoring-service application. This implementation offers methods
 * that can be used by the webservice's end client to make calls for alert data.
 * 
 * @author Sandeep Reddy,Murthy
 * @version 1.0
 */

@Component
public class AlertDataResponderServiceImpl implements AlertDataResponderService {

	private static final Logger logger = LoggerFactory
			.getLogger(AlertDataResponderServiceImpl.class);

	@Autowired
	private AlertListDao alertListDao;

	/**
	 * getAlerts method is used to make a call for data from the end webservice
	 * client.The data consists of list of alerts represented in the form of a
	 * json
	 * 
	 * @return A Response object wrapping the List of alerts data
	 * @deprecated this method would be deprecated from the final cut
	 */
	public Response getSplunkAlerts() {
		try {
			return Response.ok(alertListDao.getSplunkAlerts()).build();
		} catch (Exception e) {
			logger.warn(SOURCE_TYPE,
					"Error occured while calling Splunk serivce",
					e.getMessage());
			return Response
					.status(Response.Status.SERVICE_UNAVAILABLE)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods",
							"POST, GET, OPTIONS, DELETE, PUT")
					.header("Access-Control-Max-Age", "100000").build();
		}

	}

	@Override
	public Response getAlerts(String alertName, long start, int limit) {
		try {
			return Response
					.ok(alertListDao.getAlerts(alertName, start, limit))
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods",
							"POST, GET, OPTIONS, DELETE, PUT")
					.header("Access-Control-Max-Age", "100000").build();
		} catch (AuthorizationException e) {
			logger.warn(SOURCE_TYPE,
					"Error occured while calling alert serivce", e.getMessage());
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();

		} catch (Exception e) {
			logger.warn(SOURCE_TYPE,
					"Error occured while calling alert serivce", e.getMessage());
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();

		}

	}

	@Override
	public Response getAlerts(String alertName, long fromDate, long toDate) {
		try {
			return Response
					.ok(alertListDao.getAlerts(alertName, fromDate, toDate))
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods",
							"POST, GET, OPTIONS, DELETE, PUT")
					.header("Access-Control-Max-Age", "100000").build();
		} catch (AuthorizationException e) {
			logger.warn(SOURCE_TYPE,
					"Error occured while calling alert serivce", e.getMessage());
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();

		} catch (Exception e) {
			logger.warn(SOURCE_TYPE,
					"Error occured while calling alert serivce", e.getMessage());
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();

		}

	}

	@Override
	public Response getSetOfAlerts() {
		try {
			return Response
					.ok(alertListDao.getSetOfAlerts())
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods",
							"POST, GET, OPTIONS, DELETE, PUT")
					.header("Access-Control-Max-Age", "100000").build();
		} catch (AuthorizationException e) {
			logger.warn(SOURCE_TYPE,
					"Error occured while calling alert serivce", e.getMessage());
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();

		} catch (Exception e) {
			logger.warn(SOURCE_TYPE,
					"Error occured while calling alert serivce", e.getMessage());
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();

		}
	}

	@Override
	public Response getDates(String alertName,long start) {
		try {
			return Response
					.ok(alertListDao.getDates(alertName,start))
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods",
							"POST, GET, OPTIONS, DELETE, PUT")
					.header("Access-Control-Max-Age", "100000").build();
		} catch (AlertException e) {
			logger.warn(SOURCE_TYPE,
					"Error occured while calling alerts Dates", e.getMessage());
			return Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();

		}
	}

//	@Override
//	public Response getPaginationCount(String alertName, int limit) {
//		try {
//			return Response
//					.ok(alertListDao.getPaginationCount(alertName, limit))
//					.header("Access-Control-Allow-Origin", "*")
//					.header("Access-Control-Allow-Methods",
//							"POST, GET, OPTIONS, DELETE, PUT")
//					.header("Access-Control-Max-Age", "100000").build();
//		} catch (Throwable e) {
//			logger.warn(SOURCE_TYPE,
//					"Error occured while calling alerts Dates", e.getMessage());
//			return Response.status(Response.Status.NOT_ACCEPTABLE)
//					.entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
//		}
//	}

}
