/*
 * Copyright (C) 2015 Stubhub.
 */
/*This file will have all general urls and assumptions*/
var port = '';
var alertdata = '/bigdime-monitoring-service/rest/alertService/alertset';
var prodhost = '';
var devhost='';
var qahost='';	
var context='/bigdime-monitoring-service/rest/alertService/recentalerts?alertName=';
var datesContext='/bigdime-monitoring-service/rest/alertService/dates?alertName=';
var paginationContext='/bigdime-monitoring-service/rest/alertService/paginationcount?alertName=';
var pageSource = [{
	"env" : "Dev",
	jqxtree : devhost + port + alertdata,
	datasourceurl : {}
},{
	"env" : "Qa",
	jqxtree : qahost + port + alertdata,
	datasourceurl : {}
},{
	"env" : "Prod",
	jqxtree : prodhost + port + alertdata,
	datasourceurl : {}
}];
var dates='/bigdime-monitoring-service/rest/alertService/dates/';
                                                           