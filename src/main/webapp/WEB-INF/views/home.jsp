<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>

<form:form method="POST" action="${pageContext.request.contextPath}/message">
             <table>
                <tr>
                    <td><p>Pseudo</p></td>
                    <td><input name="pseudo"/></td>
                </tr>
                <tr>
                    <td><p>Contenu</p></td>
                    <td><input name="content"/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </form:form>

</body>
</html>
