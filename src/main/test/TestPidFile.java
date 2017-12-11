import sun.management.VMManagement;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by boot on 8/1/16.
 */
public class TestFilePath {


    public static void main(String[] args) {
        String file = "/home/boot/////jetty.sh";

        File f = new File(file);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line = "";
            while((line = br.readLine()) != null){
                System.out.println(line);
            }

            System.out.println("=================== Get PID =====================");
            System.out.println(getPID());
            createPidFile();

            System.out.println("=================== Get PID Ex ===================");
            System.out.println(getPIDEx());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static long getPID(){
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(processName);
        return Long.parseLong(processName.split("@")[0]);
    }

    private static String getPIDEx(){
        String rtn = null;
        try{
            RuntimeMXBean rmx = ManagementFactory.getRuntimeMXBean();
            Field jvmField = rmx.getClass().getDeclaredField("jvm");
            jvmField.setAccessible(true);
            VMManagement vmm = (VMManagement) jvmField.get(rmx);
            Method method = vmm.getClass().getDeclaredMethod("getProcessId");
            method.setAccessible(true);
            Integer processId = (Integer)method.invoke(vmm);
            rtn = String.valueOf(processId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rtn;
    }

    private static void createPidFile(){
        FileOutputStream fos = null ;
        OutputStreamWriter osw = null ;
        try {
            long pid = getPID();
            String pidFileName = "pid";
            fos = new FileOutputStream(pidFileName);
            osw = new OutputStreamWriter(fos,"UTF8");
            osw.write(String.valueOf(pid));
            osw.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(osw != null){
                    osw.close();
                }
                if(fos!=null){
                    fos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



}
