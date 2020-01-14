package cg.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class sshMain {

	public static void main(String[] args) {
		/*
		 * 在使用时，首先需要先建立一个Connection对象，构造函数有如下两种情况：
		//准备一个新Connection对象，然后可以使用该对象建立与指定SSH-2服务器的连接。
		Connection(java.lang.String hostname)
		//准备一个新Connection对象，然后可以使用该对象建立与指定SSH-2服务器的连接
		Connection(java.lang.String hostname, int port)      
		        然后通过调用Connection对象的connect()方法进行连接，
		接着需要通过authenticateWithPassword()方法进行用户名和密码验证，这样基本完成与服务器的连接。
			执行具体的命令时通过开启Session来实现的，通过调用Connection对象的openSession()方法来获取一个Session对象，
			通过调用Session对象的execCommand()方法执行具体的命令，需要注意的是一个Session对象只能远程执行一条语句，所以如果读者要执行多条命令，
			有两种选择，
					一种是将命令组合成一条命令，执行一次，另一种方法是执行一条命令后就关闭Session对象，但是不要关闭Connection对象，
					继续开启一个新的Session对象来执行下一条命令，依次类推，执行完所有的命令，使用后记得关闭Connection对象。

			由于上面附有有文档的接口，所以只是阐述的调用的方法，没有说明每个方法所需要的参数，读者可根据下面的代码案例进行理解，在使用时还是建议根据文档接口选择适合自己的方法：
		 */

		String hostname = "192.168.3.29";
		String username = "root";
		String password = "bus365_0502";

		try {
			Connection conn = new Connection(hostname);
			conn.connect();
			//进行身份认证
			boolean isAuthenticated = conn.authenticateWithPassword(username,password);
			if (isAuthenticated == false){
				throw new IOException("Authentication failed.");
			}
			//开启一个Session
			Session sess = conn.openSession();
			//执行具体命令
			sess.execCommand("cat anaconda-ks.cfg");
			//获取返回输出
			InputStream stdout = new StreamGobbler(sess.getStdout());
			//返回错误输出
			InputStream stderr = new StreamGobbler(sess.getStderr());
			BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
			BufferedReader stderrReader = new BufferedReader( new InputStreamReader(stderr));

			System.out.println("Here is the output from stdout:");
			while (true) {
				String line = stdoutReader.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}

			System.out.println("Here is the output from stderr:");
			while (true) {
				String line = stderrReader.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}
			//关闭Session
			sess.close();
			//关闭Connection
			conn.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(2);
		}

	}

}
