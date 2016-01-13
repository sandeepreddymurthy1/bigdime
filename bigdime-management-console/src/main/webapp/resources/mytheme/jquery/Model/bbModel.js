/*
 * Copyright (C) 2015 Stubhub.
 */
var model = Backbone.Model.extend({});
var radioButtonModel = Backbone.Model.extend({});
var tableRows; 
var initialload = [];
var jqxtreedata = [];
var initialloaddata = [];
var sourcedata =null;
var devHost='';
var qaHost='';
var prodHost='';
var devPort='';
var qaPort='';
var prodPort='';
var jqxtree='';  
function getjqxTree(selectedDropDownBox) { 
	$.each(pageSource, function(keys, values) {
		if (values.env.toLowerCase() == selected.env.toLowerCase()) {
			
			$.get(jqxtree, function(data) { 
				setJqTree(data);	
			});
			$.ajax({
			  dataType: "json",
			  url: jqxtree,
			  async: false,
			  success: function(data) { 
					for(var i=0;i<data.length;i++){
					datasourcesArray.push(data[i].label);
					var tmp=[];
					tmp[0]=Date.now()-6048000000;
					dateArrayPerApplication[datasourcesArray[i]]=tmp;	
				}
			}
		});
			
			var host='';
			var port='';
			if(selected.env.toLowerCase()=="dev"){
				host=devHost;
				port=devPort;
			}else if(selected.env.toLowerCase()=="qa"){
				host=qaHost;
				port=qaPort;
			}else if(selected.env.toLowerCase()=="prod"){
				host=prodHost;
				port=prodPort;
			}  
	$.each(datasourcesArray, function(keys, values) {
		var alertName=values;
		$.ajax({
			  dataType: "json",
			  url: host+port+datesContext+alertName+'&start='+Date.now(),
			  async: false,
			  success: function(data) { 
			      for(var i=0;i<datasourcesArray.length;i++){
			    	  if(datasourcesArray[i]==alertName){
			    		 dateArrayPerApplication[datasourcesArray[i]]=data;	             
			    	}
				}
			}
		});
	});	
			
		}
	});
}

function getpageCount(){
	$.get(jqxtree, function(data) { 
		for(var i=0;i<data.length;i++){
		datasourcesArray.push(data[i].label);
	}
	for(var j=0;j<datasourcesArray.length;j++){
//Get pagination count for each application and put them in the paginationCountPerApplication
			paginationCountPerApplication[j]=setPaginationCount(datasourcesArray[j]);
	}					
	});
}
function getApplicationProperties() {
		 $.ajax({
			  dataType: "json",
			  url: 'propertyService/applicationproperties',
			  async: false,
			  success: function(data) { 
				     devHost=data.devHost;
					 qaHost=data.qaHost;
					 prodHost=data.prodHost;
					 devPort=data.devPort;
					 qaPort=data.qaPort;
					 prodPort=data.prodPort;
					 itemsOnPage=data.rowsPerPage;
					  if(selected.env.toLowerCase()=='dev') {  	   
			    	   jqxtree=data.devHost + data.devPort + alertdata
					  }
				       if(selected.env.toLowerCase()=='qa'){	   
			    	   jqxtree=data.qaHost + data.qaPort + alertdata
					  }
			          if(selected.env.toLowerCase()=='prod') { 	   
			    	   jqxtree=data.prodHost + data.prodPort + alertdata
					 }          
			      }
				  
			});
}

function setInitialdisplay() {
	for (var i = 0; i < pageSource.length; i++) {
		if (pageSource[i].env.toUpperCase() == selected.env.toUpperCase()) {
			if (typeof (pageSource[i][selected.tree.toLowerCase() + "url"]) == "object") {			
				var initialsource = [ {"datasource" : []} ];
				var totallength = Object.keys(pageSource[i][selected.tree.toLowerCase()+ "url"]).length;
				var i,j = 0;
				var tmpjsondata=null;
				var alertcount=0;
				$.each(pageSource[i][selected.tree.toLowerCase()+ "url"],
								function(keys, values) {
									$.get(values,function(jsondata) {
										tmpjsondata=jsondata;
										for(i=0;i<tmpjsondata.length;i++){
											alertcount=alertcount+tmpjsondata[i].raisedAlerts.length
										}
														initialsource[0].datasource
																.push({
																	"datasource" : keys,
																	"alerts" : alertcount
																})
														j++;alertcount=0;i=0;
														
														if (j == totallength) {
															var detailsTableInitalLoad = new detailsTableViewInitalLoad(
																	{
																		collection : initialsource
																	});
															detailsTableInitalLoad
																	.render();
														}
													})

								})

			} else {
				collection = Backbone.Collection.extend({
					url : pageSource[i][selected.tree.toLowerCase() + "url"],
					model : radioButtonModel,
					parse : function(response) {
						return response;
					}
				});
				initialsource = new collection;
				initialsource
						.fetch({
							success : function(values) {
								var detailsTableInitalLoad = new detailsTableViewInitalLoad(
										{
											collection : initialsource.toJSON()
										});
								detailsTableInitalLoad.render();
							}
						});
			}
			break;
		}
	}
}
function getData(event,itemsOnPage) {
	if (selected.tree == "" || selected.tree == null
			|| selected.tree == undefined || selected.tree=="Datasource") {
		return false;
	}
	var host;
	if(selected.env.toUpperCase().trim()=='PROD'){
		host=prodHost+prodPort;
	}else if(selected.env.toUpperCase().trim()=='DEV'){
	   host=devHost+devPort;	
	}else if(selected.env.toUpperCase().trim()=='QA'){
	   host=qaHost+qaPort;	
	}
	var getURLSFrom=[{
	type:selected.tree,
	env : selected.env,
	alertName:selected.tree,
	AlertUrl :host+context+selected.tree+'&start='+event+'&limit='+itemsOnPage
    }];
	switch (parseInt(selected.tab)) {
	case 0:
		getAlertDetails(getURLSFrom[0].AlertUrl);
		break;
	}
}

function getAlertDetails(urlsToFetch) {
	collection = Backbone.Collection.extend({
		url : urlsToFetch,
		model : model,
		parse : function(response) {
			return response;
		}
	});
	source = new collection;
	source.fetch({
		success : function(values) {
			var AlertsTable = new Alertview({
				collection : source
			});
			AlertsTable.render();
		}
	});

}