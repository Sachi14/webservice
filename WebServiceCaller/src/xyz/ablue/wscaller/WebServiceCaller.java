package xyz.ablue.wscaller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import cn.zixizixi.www.util.ConsoleDialog;
import cn.zixizixi.www.util.StrUtils;

public class WebServiceCaller {
	public static Font pubFont = new Font("Microsoft Yahei", Font.PLAIN, 12);
	boolean packFrame = false;
	private static String title = "简易 WebService 调用/测试工具 - WebService Caller";
	private static String wsdl = "";
	// Construct the application
	public WebServiceCaller() {
		MainFrame frame = new MainFrame(title, wsdl);
		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their
		// layout
		if (packFrame) {
			frame.pack(); // 自动适应大小
		} else {
			frame.validate(); // 重新调整大小
		}
		// 窗口居中
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
            splash("正在启动，请稍候");
            new ConsoleDialog(title, false); // 初始化并隐藏日志窗口
			String version = StrUtils.JVM_VERSION;
			if (version != null) {
				ConsoleDialog.showDebug("JVM Version: " + version);
				// 检查 JVM 版本, 需要 1.3 或更高版本 25.144-b01
				try {
					int i = version.indexOf('.'); // 2
					int v1 = Integer.parseInt(version.substring(0, i)); // 25
					int j = version.indexOf('.', i + 1); // -1
					if (j > i) {
						int v2 = Integer.parseInt(version.substring(i + 1, j));
						if (v1 < 1 || (v1 == 1 && v2 < 3)) {
							JOptionPane.showMessageDialog(null, "需要 JVM 1.3 或更高版本，请更新您的 Java 运行环境！", "错误信息",
									JOptionPane.ERROR_MESSAGE);
							System.exit(-1);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					ConsoleDialog.showError("Check JVM: " + ex.getMessage());
				}
			}
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				String names[] = { "Label", "MenuItem", "CheckBoxMenuItem",/* "CheckBox", "PopupMenu", "JRadioButtonMenuItem", */
						"ComboBox", "Button", "ScrollPane", "Tree", "TabbedPane", "EditorPane",
						"TitledBorder", "Menu", "TextArea", "OptionPane", "Panel", "ToolTip", "List", 
						/* "MenuBar", "ToolBar", "ToggleButton",  "ProgressBar", "TableHeader", */ 
						/* "ColorChooser", "PasswordField", */"TextField", "Table", "Viewport",
						/* "RadioButtonMenuItem","RadioButton", */ "DesktopPane", "InternalFrame" };
				for (String item : names) {
					UIManager.put(item + ".font", pubFont);
				}
			} catch (Exception e) {
				ConsoleDialog.showError(e);
			}
	        try {
	            final Options options = new Options();
	            options.addOption("w", "wsdl", true, "WebService WSDL location.");
	            final CommandLineParser commandLineParser = new PosixParser();
	            CommandLine commandLine = commandLineParser.parse(options, args);
	            if (commandLine.hasOption("w")) {
	                wsdl = commandLine.getOptionValue("w");
	            }
	        } catch (final ParseException e) {
	        }
	        
			new WebServiceCaller();
		} catch (Exception e) {
			ConsoleDialog.showError(e);
			e.printStackTrace();
		}
	}

    private static void splash(String message) {
        new Thread() { // 用于运行SplashScreen的线程
            public void run() {
                try {
                    SplashScreen splash = SplashScreen.getSplashScreen();
                    Graphics2D g = splash.createGraphics();
                    g.setColor(new Color(1, 170, 237));
                    g.setFont(new Font("Lucaida Console", Font.PLAIN, 12));
                    // g.drawString(splash.getBounds().toString(), 10, 30); // SplashScreen 在屏幕的位置，大小
                    // g.drawString(splash.getSize().toString(), 10, 50); // SplashScreen 的大小
                    // g.drawString(splash.getImageURL().toString(), 10, 70); // 当前显示的图片
                    
                    String[] sNames = { "Java Version", "User Name", "OS Name", "App Path" };
                    String[] sValue = { (StrUtils.J_VERSION/*+" ("+StrUtils.J_HOME+")"*/), StrUtils.U_NAME, 
                                        (StrUtils.S_NAME + " (" + StrUtils.S_ARCH + ")"), StrUtils.U_DIR };
                    for (int i = 0; i < sNames.length; i++) {
                        g.drawString(sNames[i] + ": " + sValue[i], 10, (210 + i * 20));
                        splash.update(); // 刷新以上内容到屏幕
                    }

                    int fontSize = 14, showX = 10, showY = 20;
                    g.setFont(new Font("Lucaida Console", Font.PLAIN, fontSize));
                    g.drawString(message, showX, showY); // 显示信息
                    splash.update();
                    for (int i = 0; i < 6; i++) {
                        g.drawString("．", (showX + message.length() * fontSize + i * fontSize), showY);
                        splash.update(); // 刷新以上内容到屏幕
                        Thread.sleep(200);
                    }
                } catch (Exception e) {
    				ConsoleDialog.showError(e);
                    e.printStackTrace();
                }
            }
        }.start();
        try {
            Thread.sleep(2000); // 决定 SplashScreen 显示时间长短.
        } catch (Exception e) {
			ConsoleDialog.showError(e);
        }
    }
}