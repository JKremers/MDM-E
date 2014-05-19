/* Author: Joost Kremers (jkremers@Deloitte.nl)
 * Institute: Radboud University Nijmegen & Deloitte Cyber Risk Services
 * Title: Mobile Device Management Evaluator */
package com.mdme;

import java.io.File;
import java.util.List;

import com.mdme.R;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//This is the main class for MDM-E
public class MainScreen extends Activity  {

	//UI Buttons
	Button b1;
	Button b2;
	Button b3;
	Button b4;
	//Related to DeviceAdmin
	DevicePolicyManager dpm;
	ComponentName admin;
	//Policy variables
	String packageName;
	String versionName;
	String CameraDisabled;
	Integer KeyguardDisabledFeatures;
	String MaximumFailedPasswordsForWipe;
	String MaximumTimeToLock;
	String PasswordExpiration;
	String PasswordHistoryLength;
	String PasswordMinimumLength;
	String PasswordMinimumLowerCase;
	String PasswordMinimumNonLetter;
	String PasswordMinimumNumeric;
	String PasswordMinimumSymbols;
	String PasswordMinimumUpperCase;
	String PasswordQuality;
	String StorageEncryption;
	String StorageEncryptionStatus;
	String isAdminActive;
	
	//Starts the UI and finds the device administrator
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		
		getDeviceAdmin();
	}
	
	//When the app is resumed, checks for device admin.
	protected void onResume(Bundle savedInstanceState){
		getDeviceAdmin();
	}
	
	//When the app is started, checks for device admin.
	protected void onStart(Bundle savedInstanceState){
		getDeviceAdmin();
	}
	
	//Binds the device's device admin to the global variable admin. Exception when no device admin is set.
	private void getDeviceAdmin()
	{
		try{
			dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
			List<ComponentName> list = dpm.getActiveAdmins();
			admin = list.get(0);
			addListenerOnButton();
		}
		catch(NullPointerException e ){
			Log.e("MDME", "No MDM found!");
		}
		finally{
			
		}
	}
	
	//Starts button-listener.
	public void addListenerOnButton() {
		 
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		b3 = (Button) findViewById(R.id.button3);
		b4 = (Button) findViewById(R.id.button4);
 
		b1.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				try {
					policyTest();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		b2.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				detect();
			}
		});
		b3.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				getIPC(admin);
			}
		});
		b4.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				getPermissions(admin);
			}
		});
 
	}
	
	//Prints all of the policies.
	private void policyTest() throws NameNotFoundException{
		getPolicy();
	    System.out.println("ActiveAdmin name: " + packageName);
	    System.out.println("ActiveAdmin versionName: " + versionName);

	    System.out.println("Policy summary:");
	    System.out.println("Camera disabled: " + CameraDisabled);
	    System.out.println("Keyguard flags: " + KeyguardDisabledFeatures);
	    System.out.println("# before wipe: " + MaximumFailedPasswordsForWipe);
	    System.out.println("Time before wipe: " + MaximumTimeToLock);
	    System.out.println("Password expiration in: " + PasswordExpiration);
	    System.out.println("Password history length: " + PasswordHistoryLength);
	    System.out.println("Minimum password length: " + PasswordMinimumLength);
	    System.out.println("Minimum lowercase: " + PasswordMinimumLowerCase);
	    System.out.println("Minimum nonletter: " + PasswordMinimumNonLetter);
	    System.out.println("Minimum numeric: " + PasswordMinimumNumeric);
	    System.out.println("Minimum symbol: " + PasswordMinimumSymbols);
	    System.out.println("Minimum uppercase: " + PasswordMinimumUpperCase);
	    System.out.println("Password quality: " + PasswordQuality);
	    System.out.println("Encryption: " + StorageEncryption);
	    System.out.println("Encryption: " + StorageEncryptionStatus);
	    System.out.println("Is active: " + isAdminActive);
	    System.out.println("---------------------");
	}
	
	//Binds all of the policy info to global vars
	private void getPolicy() throws NameNotFoundException {
		packageName = admin.getPackageName();
		versionName = getPackageManager().getPackageInfo(packageName, 0).versionName;
		CameraDisabled = String.valueOf(dpm.getCameraDisabled(admin));
		KeyguardDisabledFeatures = dpm.getKeyguardDisabledFeatures(admin);
		MaximumFailedPasswordsForWipe = String.valueOf(dpm.getMaximumFailedPasswordsForWipe(admin));
		MaximumTimeToLock = String.valueOf(dpm.getMaximumTimeToLock(admin));
		PasswordExpiration = String.valueOf(dpm.getPasswordExpiration(admin));
		PasswordHistoryLength = String.valueOf(dpm.getPasswordHistoryLength(admin));
		PasswordMinimumLength = String.valueOf(dpm.getPasswordMinimumLength(admin));
		PasswordMinimumLowerCase = String.valueOf(dpm.getPasswordMinimumLowerCase(admin));
		PasswordMinimumNonLetter = String.valueOf(dpm.getPasswordMinimumNonLetter(admin));
		PasswordMinimumNumeric = String.valueOf(dpm.getPasswordMinimumNumeric(admin));
		PasswordMinimumSymbols = String.valueOf(dpm.getPasswordMinimumSymbols(admin));
		PasswordMinimumUpperCase = String.valueOf(dpm.getPasswordMinimumUpperCase(admin));
		PasswordQuality = String.valueOf(dpm.getPasswordQuality(admin));
		StorageEncryption = String.valueOf(dpm.getStorageEncryption(admin));
		StorageEncryptionStatus = String.valueOf(dpm.getStorageEncryptionStatus());
		isAdminActive = String.valueOf(dpm.isAdminActive(admin));
	}

	//Prints vulnerable settings
	private void detect(){
		System.out.println("USB Debug enabled? " + detectUSBDebug());
		System.out.println("Non-market apps allowed? " + detectNonMarket());
		System.out.println("Root access enabled? " + detectRoot());
	    System.out.println("---------------------");
	}

	//Checks for Unknown Sources
	private boolean detectNonMarket() {
		int NonMarket = Settings.Global.getInt(getApplicationContext().getContentResolver(), Settings.Global.INSTALL_NON_MARKET_APPS , 0); 
		return NonMarket == 1;
	}

	//Various methods to detect rooting. Adding a new method can be done easily by adding a new letter
	private boolean detectRoot() {
		boolean a = false;
		boolean b = false;
		boolean c = false;
		boolean d = false;
		boolean e = false;
		boolean f = false;
		boolean g = false;
		boolean h = false;
		String str = Build.TAGS;
		
		if((str != null) && (str.contains("test-keys")))
			a = true;
		
		if(new File("/system/app/Superuser.apk").exists())
			b = true;
		
	    String[] arrayOfString = { "/usr/bin/su", "/usr/sbin/su", "/bin/su", "/sbin/su" };
	    int i = arrayOfString.length;
	    for (int j = 0;; j++)
	    {
	      if (j >= i) {
	    	  break;
	      }
	      String str2 = arrayOfString[j];
	      if (new File(str2).exists())
	      {
	    	  c = true;
	    	  break;
	      }
	    }
	    
	    if(!new File("/etc/security/otacerts.zip").exists())
			d = true;
	    
	    return a | b | c | d | e | f | g | h;
	}

	//Detects USB-debugging setting
	private boolean detectUSBDebug() {
		int USBDebug = Settings.Global.getInt(getApplicationContext().getContentResolver(), Settings.Global.ADB_ENABLED , 0); 
		return USBDebug == 1;
	}
	
	//Shows all requested permissions
	private void getPermissions(ComponentName name){
		PackageManager pm = getPackageManager();
		try {
	        PackageInfo info = pm.getPackageInfo(name.getPackageName(), PackageManager.GET_PERMISSIONS);
	        if (info.requestedPermissions != null) {
	            for (String p : info.requestedPermissions) {
	            	System.out.println("The following permission was requested: ");
	            	System.out.println(p);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    System.out.println("---------------------");
	}
	
	//Checks for exported IPC-componenets
	private void getIPC(ComponentName name){
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(name.getPackageName(), PackageManager.GET_DISABLED_COMPONENTS
					| PackageManager.GET_ACTIVITIES
					| PackageManager.GET_RECEIVERS
					| PackageManager.GET_INSTRUMENTATION
					| PackageManager.GET_PROVIDERS
					| PackageManager.GET_SERVICES
					);
			for(int i = 0; i < pi.activities.length; i++){
				if(pi.activities[i].exported){
					System.out.println("Exported activity found. ");
					ComponentName cn = new ComponentName(pi.packageName, pi.activities[i].name);
	
					//Uncommenting this can send intents to the found exported activities
					//Intent sendIntent = new Intent();
					//sendIntent.setFlags(268435456);
					//sendIntent.setComponent(cn);
					//startActivity(sendIntent);
					System.out.println(cn);
				}
			}
		    System.out.println("---------------------");
			for(int i = 0; i < pi.receivers.length; i++){
				if(pi.receivers[i].exported){
					System.out.println("Exported receiver found. ");
					ComponentName cn = new ComponentName(pi.packageName, pi.receivers[i].name);
					System.out.println(cn);
				}
				
			}
		    System.out.println("---------------------");
			for(int i = 0; i < pi.services.length; i++){
				if(pi.services[i].exported){
					System.out.println("Exported service found. ");
					ComponentName cn = new ComponentName(pi.packageName, pi.services[i].name);
					System.out.println(cn);
				}
			}
		    System.out.println("---------------------");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
