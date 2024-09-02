package groupbee.employee.service.xml;

import lombok.NoArgsConstructor;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.stereotype.Service;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class OdooClient {
    private static final String db = "groupbee";
    private static final String url = "http://211.188.52.233:8069";
    private static final String username = "admin@groupbee.co.kr";
    private static final String password = "p@ssw0rd";

    public static Object[] employeeInfo(String email) throws MalformedURLException, XmlRpcException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url + "/xmlrpc/2/common"));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        // 사용자 인증
        int uid = (int) client.execute("authenticate", new Object[]{db, username, password, new HashMap<>()});

        if (uid > 0) {
            System.out.println("Odoo 인증 성공! UID: " + uid);
        } else {
            System.out.println("Odoo 인증 실패");
            return null;
        }

        XmlRpcClient objectClient = new XmlRpcClient();
        objectClient.setConfig(new XmlRpcClientConfigImpl() {{
            setServerURL(new URL(url + "/xmlrpc/2/object"));
        }});

        // 사원 정보 가져오기
        Object[] searchParams = new Object[]{
                db, uid, password, "hr.employee", "search_read",
                new Object[]{new Object[]{
                        new Object[]{"work_email", "=", email}
                }}, // 조건을 넣을 수 있습니다. 예: new Object[]{"name", "=", "John Doe"}
                new HashMap<String, Object>() {{
                    put("fields", new String[]{"name","user_id","department_id","company_id","id","identification_id","barcode","first_contract_date"});
                }}
        };

        Object[] employees = (Object[]) objectClient.execute("execute_kw", searchParams);

//         사원 정보 출력
        for (Object employee : employees) {
            Map<String, Object> emp = (Map<String, Object>) employee;
            System.out.println(emp);
        }


        return employees;
    }
    public static Object[] employeeList() throws MalformedURLException, XmlRpcException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url + "/xmlrpc/2/common"));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        // 사용자 인증
        int uid = (int) client.execute("authenticate", new Object[]{db, username, password, new HashMap<>()});

        if (uid > 0) {
            System.out.println("Odoo 인증 성공! UID: " + uid);
        } else {
            System.out.println("Odoo 인증 실패");
            return null;
        }

        XmlRpcClient objectClient = new XmlRpcClient();
        objectClient.setConfig(new XmlRpcClientConfigImpl() {{
            setServerURL(new URL(url + "/xmlrpc/2/object"));
        }});

        // 사원 정보 가져오기
        Object[] searchParams = new Object[]{
                db, uid, password, "hr.employee", "search_read",
                new Object[]{new Object[]{
                        new Object[]{"company_id", "=", 1}
                }}, // 조건을 넣을 수 있습니다. 예: new Object[]{"name", "=", "John Doe"}
                new HashMap<String, Object>() {{
                    put("fields", new String[]{"identification_id","name","first_contract_date","company_id","barcode"});
                }}
        };

        Object[] employees = (Object[]) objectClient.execute("execute_kw", searchParams);

        return employees;
    }

    public static Object[] employeeInfoById(String idNumber) throws MalformedURLException, XmlRpcException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url + "/xmlrpc/2/common"));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        // 사용자 인증
        int uid = (int) client.execute("authenticate", new Object[]{db, username, password, new HashMap<>()});

        if (uid > 0) {
            System.out.println("Odoo 인증 성공! UID: " + uid);
        } else {
            System.out.println("Odoo 인증 실패");
            return null;
        }

        XmlRpcClient objectClient = new XmlRpcClient();
        objectClient.setConfig(new XmlRpcClientConfigImpl() {{
            setServerURL(new URL(url + "/xmlrpc/2/object"));
        }});

        // 사원 정보 가져오기
        Object[] searchParams = new Object[]{
                db, uid, password, "hr.employee", "search_read",
                new Object[]{new Object[]{
                        new Object[]{"barcode", "=", idNumber}
                }}, // 조건을 넣을 수 있습니다. 예: new Object[]{"name", "=", "John Doe"}
                new HashMap<String, Object>() {{
                    put("fields", new String[]{"name","user_id","department_id","company_id","id","identification_id","barcode","first_contract_date"});
                }}
        };

        Object[] employees = (Object[]) objectClient.execute("execute_kw", searchParams);

        return employees;
    }
}
