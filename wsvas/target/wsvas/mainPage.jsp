<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Expires" CONTENT="0">
    <meta http-equiv="Cache-Control" CONTENT="no-cache">
    <meta http-equiv="Pragma" CONTENT="no-cache">
    <%@include file="include.jsp" %>
    <script type="text/javascript" src="<c:url value='/js/FusionCharts.js'/>"></script>
    <title>Wing Tech</title>
</head>
<body>
<div id="chart-container" style="float: left;">FusionCharts will load here...</div>
<div id="chart-container1" style="float: left;">FusionCharts will load here...</div>
</body>
</html>
<script type="text/javascript">
    $(function () {
        var myChart = new FusionCharts("<c:url value='/charts/Column3D.swf'/>", "myChart", "500", "400", "0");
        myChart.setJSONData({
            "chart": {
                "caption": "Social Media Content Distribution Analysis",
                "subcaption": "For 2013",
                "yaxismaxvalue": "100",
                "xaxisname": "What % of content distributed on this channel?",
                "numbersuffix": "%",
                "showvalues": "1",
                "bgcolor": "#FFFFFF",
                "basefontcolor": "#400D1B",
                "showalternatehgridcolor": "0",
                "showcanvasbase": "0",
                "labeldisplay": "WRAP",
                "divlinecolor": "#400D1B",
                "divlinealpha": "15",
                "canvasbgcolor": "#FFFFFF",
                "showcanvasbg": "0",
                "showshadow": "0",
                "animation": "0",
                "palettecolors": "#BE3243,#986667,#BE6F71,#CB999A,#DFC0B1,#E0D0D0",
                "showborder": "0"
            },
            "data": [
                {
                    "label": "Twitter",
                    "value": "78"
                },
                {
                    "label": "Facebook",
                    "value": "75"
                },
                {
                    "label": "LinkedIn",
                    "value": "30"
                },
                {
                    "label": "Wordpress",
                    "value": "22"
                },
                {
                    "label": "Flickr",
                    "value": "13"
                },
                {
                    "label": "Tumblr",
                    "value": "11"
                }
            ]
        });
        myChart.render('chart-container');

        var myChart1 = new FusionCharts("<c:url value='/charts/MSLine.swf'/>", "myChart1", "500", "400", "0");
        myChart1.setJSONData(
                {
                    "chart": {
                        "caption": "Sales - 2012 v 2013",
                        "numberprefix": "$",
                        "plotgradientcolor": "",
                        "bgcolor": "FFFFFF",
                        "showalternatehgridcolor": "0",
                        "divlinecolor": "CCCCCC",
                        "showvalues": "0",
                        "showcanvasborder": "0",
                        "canvasborderalpha": "0",
                        "canvasbordercolor": "CCCCCC",
                        "canvasborderthickness": "1",
                        "yaxismaxvalue": "30000",
                        "captionpadding": "30",
                        "linethickness": "3",
                        "yaxisvaluespadding": "15",
                        "legendshadow": "0",
                        "legendborderalpha": "0",
                        "palettecolors": "#f8bd19,#008ee4,#33bdda,#e44a00,#6baa01,#583e78",
                        "showborder": "0"
                    },
                    "categories": [
                        {
                            "category": [
                                {
                                    "label": "Jan"
                                },
                                {
                                    "label": "Feb"
                                },
                                {
                                    "label": "Mar"
                                },
                                {
                                    "label": "Apr"
                                },
                                {
                                    "label": "May"
                                },
                                {
                                    "label": "Jun"
                                },
                                {
                                    "label": "Jul"
                                },
                                {
                                    "label": "Aug"
                                },
                                {
                                    "label": "Sep"
                                },
                                {
                                    "label": "Oct"
                                },
                                {
                                    "label": "Nov"
                                },
                                {
                                    "label": "Dec"
                                }
                            ]
                        }
                    ],
                    "dataset": [
                        {
                            "seriesname": "2013",
                            "data": [
                                {
                                    "value": "22400"
                                },
                                {
                                    "value": "24800"
                                },
                                {
                                    "value": "21800"
                                },
                                {
                                    "value": "21800"
                                },
                                {
                                    "value": "24600"
                                },
                                {
                                    "value": "27600"
                                },
                                {
                                    "value": "26800"
                                },
                                {
                                    "value": "27700"
                                },
                                {
                                    "value": "23700"
                                },
                                {
                                    "value": "25900"
                                },
                                {
                                    "value": "26800"
                                },
                                {
                                    "value": "24800"
                                }
                            ]
                        },
                        {
                            "seriesname": "2012",
                            "data": [
                                {
                                    "value": "10000"
                                },
                                {
                                    "value": "11500"
                                },
                                {
                                    "value": "12500"
                                },
                                {
                                    "value": "15000"
                                },
                                {
                                    "value": "16000"
                                },
                                {
                                    "value": "17600"
                                },
                                {
                                    "value": "18800"
                                },
                                {
                                    "value": "19700"
                                },
                                {
                                    "value": "21700"
                                },
                                {
                                    "value": "21900"
                                },
                                {
                                    "value": "22900"
                                },
                                {
                                    "value": "20800"
                                }
                            ]
                        }
                    ]
                }
        );
        myChart1.render('chart-container1');
    });
</script>