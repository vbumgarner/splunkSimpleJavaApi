<html
     xmlns:c="http://java.sun.com/jsp/jstl/core"
     xmlns:fn="http://java.sun.com/jsp/jstl/functions"
     xmlns:jsp="http://java.sun.com/JSP/Page">

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--
< jsp:directive.page import="com.splunk.search.Results" />
< jsp:directive.page import="com.splunk.search.Search" />
< jsp:directive.page import="java.net.URL" />
-->

  <head>
    <title>search form example</title>

    <style type="text/css">
      <!--
        @import url("style.css");
      -->
    </style>

  </head>

  <body>

<h2>Basic search example</h2>

<form>
  <label for="server">Server</label>
  <input name="server" value="${not empty param.server ? param.server : 'https://localhost:8089'}" />
  <br />
  <label for="user">Username</label>
  <input name="user" value="${not empty param.user ? param.user : 'admin'}" />
  <br />
  <label for="password">Password</label>
  <input type="password" name="password" value="${not empty param.password ? fn:escapeXml(param.password) : 'changeme'}" />
  <br />
  <label for="q">Search</label>
  <input name="q" value="${not empty param.q ? fn:escapeXml(param.q) : '* | head 10'}" size="100" />
  <br />
  <input type="submit" value="Go!" />
</form>

<h3>Warning: This does exactly what you tell it. It will try to draw a million results if that's what the query returns. Use <code>| head</code> liberally.</h3>

<c:if test='${not empty param.q}'>

<jsp:useBean id="search1" scope="request" class="com.splunk.search.jsp.SearchBean"> 
  <jsp:setProperty name="search1" property="server" value="${param.server}" />
  <jsp:setProperty name="search1" property="user" value="${param.user}" />
  <jsp:setProperty name="search1" property="password" value="${param.password}" />
  <jsp:setProperty name="search1" property="search" value="${param.q}" />
</jsp:useBean>

<c:set var="res1" scope="request" value="${search1.results}" />

<p>${res1.status.resultCount} rows generated from ${res1.status.eventCount} events after scanning ${res1.status.scanCount} events in ${res1.status.runDuration} seconds.</p>

<c:if test='${res1.status.resultCount gt 0}'>
    <table border="1" class="hor-minimalist-b">
      <thead>
        <tr>
          <c:forEach var="col" items="${res1.columns}">
            <th>${col}</th>
          </c:forEach>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="row" items="${res1.rows}">
          <tr>
            <c:forEach var="col" items="${res1.columns}">
              <td class="col-${col}">${row[col][0]}</td>
            </c:forEach>
          </tr>
        </c:forEach>
      </tbody>
    </table>

</c:if>
</c:if>

  </body>
</html>

