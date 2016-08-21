<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<nav class="top-main-menu navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<spring:url value='/'/>">
                <img alt="Brand" src="<spring:url value='images/AS.png'/>" style="width: 100px;">
            </a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"><a href="<spring:url value='/'/>">Home<span class="sr-only">(current)</span></a></li>
                <li><a href="<spring:url value='/projects'/>">Projects</a></li>
                <li><a href="<spring:url value='/release'/>">Release</a></li>
                <li><a href="<spring:url value='/issue'/>">Issue</a></li>
                <li><a href="<spring:url value='/history'/>">History</a></li>
                <li><a href="<spring:url value='/users'/>">User</a></li>

                <sec:authorize access="hasRole('ADMIN')">
                    <li><a href="<spring:url value='/admin'/>">Admin</a></li>
                </sec:authorize>

                <li><a href="<spring:url value='/about'/>">About</a></li>
            </ul>

        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
