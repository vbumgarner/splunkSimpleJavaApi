package com.splunk.search;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.splunk.search.rest.XMLUtils;

public class Status {
	private static final Logger logger = Logger.getLogger(Status.class);

	private Document document;

	public Status(Document d) {
		this.document = d;
	}

	// the integers
	public Integer getDiskUsage() {
		return integerValue(buildKeyPath("diskUsage"));
	}

	public Integer getDropCount() {
		return integerValue(buildKeyPath("dropCount"));
	}

	public Integer getEventAvailableCount() {
		return integerValue(buildKeyPath("eventAvailableCount"));
	}

	public Integer getEventCount() {
		return integerValue(buildKeyPath("eventCount"));
	}

	public Integer getEventIsStreaming() {
		return integerValue(buildKeyPath("eventIsStreaming"));
	}

	public Integer getEventIsTruncated() {
		return integerValue(buildKeyPath("eventIsTruncated"));
	}

	public Integer getPriority() {
		return integerValue(buildKeyPath("priority"));
	}

	public Integer getResultCount() {
		return integerValue(buildKeyPath("resultCount"));
	}

	public Integer getResultIsStreaming() {
		return integerValue(buildKeyPath("resultIsStreaming"));
	}

	public Integer getResultPreviewCount() {
		return integerValue(buildKeyPath("resultPreviewCount"));
	}

	public Integer getScanCount() {
		return integerValue(buildKeyPath("scanCount"));
	}

	public Integer getStatusBuckets() {
		return integerValue(buildKeyPath("statusBuckets"));
	}

	public Integer getTtl() {
		return integerValue(buildKeyPath("ttl"));
	}

	// the floats
	public Float getDoneProgress() {
		return floatValue(buildKeyPath("doneProgress"));
	}

	public Float getRunDuration() {
		return floatValue(buildKeyPath("runDuration"));
	}

	// the strings
	public String getSid() {
		return stringValue(buildKeyPath("sid"));
	}

	public String getDispatchState() {
		return stringValue(buildKeyPath("dispatchState"));
	}

	public String getEventSearch() {
		return stringValue(buildKeyPath("eventSearch"));
	}

	public String getEventSorting() {
		return stringValue(buildKeyPath("eventSorting"));
	}

	// dates, which we'll just return as strings for now
	public String getLatestTime() {
		return stringValue(buildKeyPath("latestTime"));
	}

	public String getEarliestTime() {
		return stringValue(buildKeyPath("earliestTime"));
	}

	// booleans
	public Boolean isDone() {
		return booleanValue(buildKeyPath("isDone"));
	}

	public Boolean isFailed() {
		return booleanValue(buildKeyPath("isFailed"));
	}

	public Boolean isFinalized() {
		return booleanValue(buildKeyPath("isFinalized"));
	}

	public Boolean isPaused() {
		return booleanValue(buildKeyPath("isPaused"));
	}

	public Boolean isPreviewEnabled() {
		return booleanValue(buildKeyPath("isPreviewEnabled"));
	}

	public Boolean isRealTimeSearch() {
		return booleanValue(buildKeyPath("isRealTimeSearch"));
	}

	public Boolean isRemoteTimeline() {
		return booleanValue(buildKeyPath("isRemoteTimeline"));
	}

	public Boolean isSaved() {
		return booleanValue(buildKeyPath("isSaved"));
	}

	public Boolean isSavedSearch() {
		return booleanValue(buildKeyPath("isSavedSearch"));
	}

	public Boolean isZombie() {
		return booleanValue(buildKeyPath("isZombie"));
	}

	// xpath stuff...
	private String buildKeyPath(String k) {
		return "/entry/content/dict/key[@name='" + k + "']/text()";
	}

	private String stringValue(String path) {
		try {
			return XMLUtils.runXpathToString(document, path);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	private Boolean booleanValue(String path) {
		try {
			String val = XMLUtils.runXpathToString(document, path);
			if (val == null || "".equals(val.trim())) {
				return null;
			}
			if (val.trim().equals("1") || val.toLowerCase().equals("true")) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	private Float floatValue(String path) {
		try {
			return new Float(XMLUtils.runXpathToString(document, path));
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	private Integer integerValue(String path) {
		try {
			return new Integer(XMLUtils.runXpathToString(document, path));
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/*
	 * <s:key name="diskUsage">1155072</s:key> <s:key
	 * name="dispatchState">DONE</s:key> <s:key
	 * name="doneProgress">1.00000</s:key> <s:key name="dropCount">0</s:key>
	 * <s:key name="earliestTime">2008-08-26T18:24:38.000-05:00</s:key> <s:key
	 * name="eventAvailableCount">21766</s:key> <s:key
	 * name="eventCount">21766</s:key> <s:key name="eventIsStreaming">1</s:key>
	 * <s:key name="eventIsTruncated">0</s:key> <s:key name="eventSearch">search
	 * *</s:key> <s:key name="eventSorting">desc</s:key> <s:key
	 * name="isDone">1</s:key> <s:key name="isFailed">0</s:key> <s:key
	 * name="isFinalized">0</s:key> <s:key name="isPaused">0</s:key> <s:key
	 * name="isPreviewEnabled">0</s:key> <s:key
	 * name="isRealTimeSearch">0</s:key> <s:key
	 * name="isRemoteTimeline">0</s:key> <s:key name="isSaved">0</s:key> <s:key
	 * name="isSavedSearch">0</s:key> <s:key name="isZombie">0</s:key> <s:key
	 * name="latestTime">1969-12-31T18:00:00.000-06:00</s:key> <s:key
	 * name="priority">5</s:key> <s:key name="resultCount">21766</s:key> <s:key
	 * name="resultIsStreaming">1</s:key> <s:key
	 * name="resultPreviewCount">21766</s:key> <s:key
	 * name="runDuration">1.667000</s:key> <s:key name="scanCount">21766</s:key>
	 * <s:key name="sid">1309385186.28</s:key> <s:key
	 * name="statusBuckets">0</s:key> <s:key name="ttl">588</s:key>
	 */
}

/*
 * <?xml-stylesheet type="text/xml" href="/static/atom.xsl"?> <entry
 * xmlns="http://www.w3.org/2005/Atom" xmlns:s="http://dev.splunk.com/ns/rest"
 * xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/"> <title>search
 * *</title> <id>https://localhost:8089/services/search/jobs/1309385186.28</id>
 * <updated>2011-06-29T17:06:28.000-05:00</updated> <link
 * href="/services/search/jobs/1309385186.28" rel="alternate"/>
 * <published>2011-06-29T17:06:26.000-05:00</published> <link
 * href="/services/search/jobs/1309385186.28/search.log" rel="log"/> <link
 * href="/services/search/jobs/1309385186.28/events" rel="events"/> <link
 * href="/services/search/jobs/1309385186.28/results" rel="results"/> <link
 * href="/services/search/jobs/1309385186.28/results_preview"
 * rel="results_preview"/> <link
 * href="/services/search/jobs/1309385186.28/timeline" rel="timeline"/> <link
 * href="/services/search/jobs/1309385186.28/summary" rel="summary"/> <link
 * href="/services/search/jobs/1309385186.28/control" rel="control"/> <author>
 * <name>admin</name> </author> <content type="text/xml"> <s:dict> <s:key
 * name="cursorTime">1969-12-31T18:00:00.000-06:00</s:key> <s:key
 * name="delegate"></s:key> <s:key name="diskUsage">1155072</s:key> <s:key
 * name="dispatchState">DONE</s:key> <s:key name="doneProgress">1.00000</s:key>
 * <s:key name="dropCount">0</s:key> <s:key
 * name="earliestTime">2008-08-26T18:24:38.000-05:00</s:key> <s:key
 * name="eventAvailableCount">21766</s:key> <s:key
 * name="eventCount">21766</s:key> <s:key name="eventFieldCount">6</s:key>
 * <s:key name="eventIsStreaming">1</s:key> <s:key
 * name="eventIsTruncated">0</s:key> <s:key name="eventSearch">search *</s:key>
 * <s:key name="eventSorting">desc</s:key> <s:key name="isDone">1</s:key> <s:key
 * name="isFailed">0</s:key> <s:key name="isFinalized">0</s:key> <s:key
 * name="isPaused">0</s:key> <s:key name="isPreviewEnabled">0</s:key> <s:key
 * name="isRealTimeSearch">0</s:key> <s:key name="isRemoteTimeline">0</s:key>
 * <s:key name="isSaved">0</s:key> <s:key name="isSavedSearch">0</s:key> <s:key
 * name="isZombie">0</s:key> <s:key name="keywords"></s:key> <s:key
 * name="label"></s:key> <s:key
 * name="latestTime">1969-12-31T18:00:00.000-06:00</s:key> <s:key
 * name="numPreviews">0</s:key> <s:key name="priority">5</s:key> <s:key
 * name="remoteSearch">litsearch * | fields keepcolorder=t "host" "index"
 * "linecount" "source" "sourcetype" "splunk_server"</s:key> <s:key
 * name="reportSearch"></s:key> <s:key name="resultCount">21766</s:key> <s:key
 * name="resultIsStreaming">1</s:key> <s:key
 * name="resultPreviewCount">21766</s:key> <s:key
 * name="runDuration">1.667000</s:key> <s:key name="scanCount">21766</s:key>
 * <s:key name="sid">1309385186.28</s:key> <s:key name="statusBuckets">0</s:key>
 * <s:key name="ttl">588</s:key> <s:key name="performance"> <s:dict> <s:key
 * name="command.fields"> <s:dict> <s:key name="duration_secs">0.016</s:key>
 * <s:key name="invocations">16</s:key> <s:key name="input_count">21766</s:key>
 * <s:key name="output_count">21766</s:key> </s:dict> </s:key> <s:key
 * name="command.search"> <s:dict> <s:key name="duration_secs">0.468</s:key>
 * <s:key name="invocations">16</s:key> <s:key name="input_count">0</s:key>
 * <s:key name="output_count">21766</s:key> </s:dict> </s:key> <s:key
 * name="command.search.fieldalias"> <s:dict> <s:key
 * name="duration_secs">0.014</s:key> <s:key name="invocations">14</s:key>
 * <s:key name="input_count">21766</s:key> <s:key
 * name="output_count">21766</s:key> </s:dict> </s:key> <s:key
 * name="command.search.index"> <s:dict> <s:key
 * name="duration_secs">0.024</s:key> <s:key name="invocations">20</s:key>
 * </s:dict> </s:key> <s:key name="command.search.kv"> <s:dict> <s:key
 * name="duration_secs">0.212</s:key> <s:key name="invocations">14</s:key>
 * </s:dict> </s:key> <s:key name="command.search.lookups"> <s:dict> <s:key
 * name="duration_secs">0.014</s:key> <s:key name="invocations">14</s:key>
 * <s:key name="input_count">21766</s:key> <s:key
 * name="output_count">21766</s:key> </s:dict> </s:key> <s:key
 * name="command.search.rawdata"> <s:dict> <s:key
 * name="duration_secs">0.223</s:key> <s:key name="invocations">14</s:key>
 * </s:dict> </s:key> <s:key name="command.search.tags"> <s:dict> <s:key
 * name="duration_secs">0.016</s:key> <s:key name="invocations">16</s:key>
 * <s:key name="input_count">21766</s:key> <s:key
 * name="output_count">21766</s:key> </s:dict> </s:key> <s:key
 * name="command.search.typer"> <s:dict> <s:key
 * name="duration_secs">0.015</s:key> <s:key name="invocations">16</s:key>
 * <s:key name="input_count">21766</s:key> <s:key
 * name="output_count">21766</s:key> </s:dict> </s:key> <s:key
 * name="dispatch.createProviderQueue"> <s:dict> <s:key
 * name="duration_secs">0.024</s:key> <s:key name="invocations">1</s:key>
 * </s:dict> </s:key> <s:key name="dispatch.evaluate"> <s:dict> <s:key
 * name="duration_secs">0.029</s:key> <s:key name="invocations">1</s:key>
 * </s:dict> </s:key> <s:key name="dispatch.evaluate.search"> <s:dict> <s:key
 * name="duration_secs">0.028</s:key> <s:key name="invocations">1</s:key>
 * </s:dict> </s:key> <s:key name="dispatch.fetch"> <s:dict> <s:key
 * name="duration_secs">0.470</s:key> <s:key name="invocations">17</s:key>
 * </s:dict> </s:key> <s:key name="dispatch.readEventsInResults"> <s:dict>
 * <s:key name="duration_secs">0.205</s:key> <s:key name="invocations">1</s:key>
 * </s:dict> </s:key> <s:key name="dispatch.stream.local"> <s:dict> <s:key
 * name="duration_secs">0.468</s:key> <s:key name="invocations">16</s:key>
 * </s:dict> </s:key> <s:key name="dispatch.timeline"> <s:dict> <s:key
 * name="duration_secs">0.517</s:key> <s:key name="invocations">17</s:key>
 * </s:dict> </s:key> </s:dict> </s:key> <s:key name="messages"> <s:dict/>
 * </s:key> <s:key name="request"> <s:dict> <s:key name="search">search
 * *</s:key> </s:dict> </s:key> <s:key name="eai:acl"> <s:dict> <s:key
 * name="perms"> <s:dict> <s:key name="read"> <s:list> <s:item>admin</s:item>
 * </s:list> </s:key> <s:key name="write"> <s:list> <s:item>admin</s:item>
 * </s:list> </s:key> </s:dict> </s:key> <s:key name="owner">admin</s:key>
 * <s:key name="modifiable">true</s:key> <s:key name="sharing">global</s:key>
 * <s:key name="app">search</s:key> <s:key name="can_write">true</s:key>
 * </s:dict> </s:key> <s:key name="searchProviders"> <s:list>
 * <s:item>minime</s:item> </s:list> </s:key> </s:dict> </content> </entry>
 */
