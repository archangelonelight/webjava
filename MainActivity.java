package com.filetran.ft;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.net.*;
import java.io.*;
import android.view.*;
import android.content.*;
import android.content.pm.*;

public class MainActivity extends Activity 
{
	EditText et,por;
	TextView iu,ippor;
	boolean g=true;
	String[] per={"android.permission.WRITE_EXTERNAL_STORAGE"};
	String gk="";
	String f="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		et = findViewById(R.id.et);
		iu = findViewById(R.id.iu);
		ippor=findViewById(R.id.ippor);
		por=findViewById(R.id.por);
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
			requestPermissions(per,80);
		}
    }
	public void tr(View v){
		g=true;
		Thread fre = new Thread(new yt());
		fre.start();
		ippor.setText("run "+et.getText().toString()+":"+por.getText().toString());
		
	}
	public void yf(View v){
		g=false;
		gk="";
		iu.setText(gk);
	}
	public void ajh(View v){
		Intent ds=new Intent(Intent.ACTION_GET_CONTENT);
		ds.setType("*/*");
		startActivityForResult(ds,7);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==7 && resultCode==RESULT_OK){
			String sk=data.getData().getPath();
			f=sk;
			Toast.makeText(this, sk, Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		if(requestCode==80 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
			Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
			df();
		}else{
			Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
		}
	}
	private void df(){
		File vfi=new File(Environment.getExternalStorageDirectory()+"/webserver");
		try{
			if(!vfi.exists()){
				vfi.mkdir();
			}
		}catch(Exception d){}
	}
	class yt implements Runnable
	{
		@Override
		public void run()
		{
			try{
				ServerSocket i = new ServerSocket();
				InetSocketAddress ht = new InetSocketAddress(et.getText().toString(),Integer.parseInt(por.getText().toString()));
				i.bind(ht);
				while(g){
					Socket to = i.accept();
					BufferedReader yt =new BufferedReader(new InputStreamReader(to.getInputStream()));
					String gh;
					String df= "";
					while((gh = yt.readLine())!=null){
						df=df+gh+"\r\n";
						if(gh.isEmpty()){
							break;
						}
					}
					gk+=df+"\n";
					String th= df.split("\n")[0];
					String pare ="";
					String ki=th.split(" ")[1];
					String hdh="";
					if(ki.equals("/")){
					    pare="index.html";
					}else{
						String[] par= ki.split("/");
						pare=par[par.length-1];
						hdh=ki.substring(0,ki.lastIndexOf("/"));
					}
					BufferedInputStream hg=new BufferedInputStream(new FileInputStream(new File(f.substring(0,f.lastIndexOf("/"))+hdh,pare)));
					OutputStream retu= to.getOutputStream();
					byte[] l= new byte[1024];
					int r=0;
					retu.write("HTTP/1.1 200 OK\r\n".getBytes());
					retu.write("\r\n".getBytes());
					while((r=hg.read(l,0,1024))!=-1){
						retu.write(l,0,r);
					}
					retu.write("\r\n\r\n".getBytes());
					retu.flush();
					hg.close();
					retu.close();
					iu.append(gk);
					gk="";
				}
				i.close();
			}catch(Exception u){
				iu.append(u.toString());
			}
		}
	}
}




