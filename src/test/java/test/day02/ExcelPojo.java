package test.day02;


import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * ClassName:ExcelPojo
 * Package:test.day02
 *
 * @Data:2022/11/1 15:54
 * @Author:tanliming2018@126.com
 */
public class ExcelPojo {
    @Excel(name = "序号(caseId)")
    private int caseId;

    @Excel(name = "接口模块(interface)")
    private String  interfaceName;

    @Excel(name = "用例标题(title)")
    private String  title;

    @Excel(name = "请求头(requestHeader)")
    private String  requestHeader;

    @Excel(name = "请求方式(method)")
    private String  method;

    @Excel(name = "接口地址(url)")
    private String  url;

    @Excel(name = "参数输入(inputParams)")
    private String  inputParams;

    @Excel(name = "期望返回结果(expected)")
    private String  expected;

    @Excel(name = "提取返回数据(extract)")
    private String extract;

    @Excel(name = "数据库校验")
    private String dbAssert;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getDbAssert() {
        return dbAssert;
    }

    public void setDbAssert(String dbAssert) {
        this.dbAssert = dbAssert;
    }

    @Override
    public String toString() {
        return "ExcelPojo{" +
                "caseId=" + caseId +
                ", interfaceName='" + interfaceName + '\'' +
                ", title='" + title + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", expected='" + expected + '\'' +
                ", extract='" + extract + '\'' +
                ", dbAssert='" + dbAssert + '\'' +
                '}';
    }
}
