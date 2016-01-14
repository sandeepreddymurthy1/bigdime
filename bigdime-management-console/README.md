### Bigdime Management console  : 
Bigdime Management console provides a clean and crisp view of alerts that have occurred in the bigdime eco system.It is seamlessly integrated with bigdime’s metadata management system  and monitoring services/Alerting system to fetch adaptor information and the relevant alerts from the persistent store.

Getting Started
=======
To get started, you need to obtain a bigdime distribution and execute the install steps: There are two ways to obtain a bigdime distribution's management console: [Download It!](#download) or [Build It!](#build).
1. Obtain a distribtion
-
<a name="download">
## Download
</a>

1. Management Console release can be downloaded from [here](https://oss.sonatype.org/content/groups/public/io/bigdime/bigdime-dist/).
2. Monitoring service release can be downloaded from [here] (https://oss.sonatype.org/content/groups/public/io/bigdime/bigdime-dist/).


-OR-
<a name="build">
## Build
</a>

1. Clone the BigDime repository to, say /opt/bigdime/repo.
2. From the /opt/bigdime/repo path, run "mvn clean package". This may take a couple of minutes to complete.
3. Verify that bigdime-management-console-${version}.war exists in /opt/bigdime/repo/bigdime-management-console/target.
4. Verify that bigdime-monitoring-service-${version}.war exists in /opt/bigdime/repo/bigdime-monitoring/bigdime-monitoring-service/target

2. Install
-
4. Download the release or build it yourself and copy the built artifact to a path, say /path/bigdime.
5. Navigate to /path/bigdime/
6. Download bigdime-management-console.war & bigdime-monitoring-service.war on to the system where these applications would be deployed.
7. Move them into the respective tomcats webapps folder. After the move you should be able to see the war files under ${CATALINA_BASE}/webapps.
8. Setup below environmental variables in the file with name "application.properties" .Create a environmental variable "ext.properties" pointing to the "application.properties" file 
### Application Properties : 
   ```
   zkEnabled=true "boolean to verify if the zookeeper is enabled"   hbase.connection.timeout="hbases connection time out value"   hbase.zookeeper.quorum="zookeeper quorums address.This would be a comma           seperated value"
   hbase.zookeeper.property.clientPort="zookeeper client port"   zookeeper.znode.parent="parent znode"   htable.pool.size="htables pool size"
   hbase.alert.table="hbase table name where alerts would be pushed"
   monitoring.devhost="dev host name" eg http://devHostName.com:
   monitoring.qahost="qa host name"   eg http://qaHostName.com:
   monitoring.prodhost="prod host name"  eg http://prodHostName.com:
   monitoring.devport="port where dev monitoring service is deployed"
   monitoring.qaport="port where qa monitoring service is deployed"
   monitoring.prodport="port where prod monitoring service is deployed"
   monitoring.numberofdays="number of days in long format" e.g. 1day=1x24x3600x1000
   ldap.platform.accountName = "ldap account name"   ldap.platform.account.pw = "ldap password for the account"   ldap.enabled = "boolean.should be true if ldap authentication is enabled"     ldap.url ="ldap servers url" eg: "ldap://ldapserverName.com:portnumber"   ldap.BASE="ldap base" eg:"OU=XXX,DC=XXX,DC=XXX"
   
    ```
3. Run
-
 1.Start the tomcat server by the below command 
  $./{CATALINA-BASE}/bin/catalina.sh start
 2.Check the logs are rolling with no issues at
  view {CATALINA-BASE}/logs/catalina.out
 3.Once the deployment is done, users can access bigdime management console via http://APPLICATION_HOST_SERVER:DEPLOYED_PORT/bigdime-management-console





