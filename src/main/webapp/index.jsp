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

<jsp:useBean id="search1" scope="request" class="com.splunk.search.jsp.SearchBean"> 
  <jsp:setProperty name="search1" property="server" value="https://minime.local.:8089" />
  <jsp:setProperty name="search1" property="user" value="admin" />
  <jsp:setProperty name="search1" property="password" value="wanker" />
  <jsp:setProperty name="search1" property="search" value="* | head 10 | top host source sourcetype" />
</jsp:useBean>

<c:set var="res1" scope="request" value="${search1.results}" />

  <head>
    <title>static search example</title>
  </head>

  <body>
    <table border="1">
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
              <td>${row[col][0]}</td>
            </c:forEach>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </body>
</html>

