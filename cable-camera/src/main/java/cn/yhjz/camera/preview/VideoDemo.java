package cn.yhjz.camera.preview;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import static cn.yhjz.camera.preview.ClientDemo.hCNetSDK;
import static cn.yhjz.camera.preview.ClientDemo.playControl;

/**
 * 视频取流预览，下载，抓图
 *
 * @create 2022-03-30-9:48
 */
public class VideoDemo {
    Timer Downloadtimer;//下载用定时器
    Timer Playbacktimer;//回放用定时器
    static FRealDataCallBack fRealDataCallBack;//预览回调函数实现
    static fPlayEScallback fPlayescallback; //裸码流回调函数
    static playDataCallBack playBackCallBack; //回放码流回调
    static int lPlay = -1;  //预览句柄
    int m_lLoadHandle;
    int iPlayBack; //回放句柄
    static File file;
    static FileOutputStream outputStream;
    static IntByReference m_lPort = new IntByReference(-1);
    static String resultFileName = "..\\Download" + new String("returnFile" + ".h264");
    static FileOutputStream fileOutputStream = null;
    static int fileLenth = 0;
    static boolean bEnd = false;

    public static void RealPlay(int userID, int iChannelNo) {
        if (userID == -1) {
            System.out.println("请先注册");
            return;
        }
        HCNetSDK.NET_DVR_PREVIEWINFO strClientInfo = new HCNetSDK.NET_DVR_PREVIEWINFO();
        strClientInfo.read();
        strClientInfo.hPlayWnd = null;  //窗口句柄，从回调取流不显示一般设置为空
        strClientInfo.lChannel = iChannelNo;  //通道号
        strClientInfo.dwStreamType = 0; //0-主码流，1-子码流，2-三码流，3-虚拟码流，以此类推
        strClientInfo.dwLinkMode = 0; //连接方式：0- TCP方式，1- UDP方式，2- 多播方式，3- RTP方式，4- RTP/RTSP，5- RTP/HTTP，6- HRUDP（可靠传输） ，7- RTSP/HTTPS，8- NPQ
        strClientInfo.bBlocked = 1;
        strClientInfo.write();

        //回调函数定义必须是全局的
        if (fRealDataCallBack == null) {
            fRealDataCallBack = new FRealDataCallBack();
        }

        //开启预览
        lPlay = hCNetSDK.NET_DVR_RealPlay_V40(userID, strClientInfo, fRealDataCallBack, null);
        if (lPlay == -1) {
            int iErr = hCNetSDK.NET_DVR_GetLastError();
            System.out.println("取流失败" + iErr);
            return;
        }
        System.out.println("取流成功");

        //设置裸码流回调函数
/*        if (fPlayescallback==null)
        {
            fPlayescallback=new fPlayEScallback();
        }
        boolean setcallback=ClientDemo.hCNetSDK.NET_DVR_SetESRealPlayCallBack(lPlay,fPlayescallback,null);
        if (setcallback==false)
        {
            System.out.println("设置裸码流回调失败，错误码："+ClientDemo.hCNetSDK.NET_DVR_GetLastError());
        }*/

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //取流解码过程中播放库从解码码流中抓图
        for (int i = 0; i < 100; i++) {
            getPicbyPlayCtrl();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        /**
         * 预览一段时间；如果要一直取流预览，需要保证程序一直运行
         */


        if (VideoDemo.lPlay >= 0) {
            if (hCNetSDK.NET_DVR_StopRealPlay(VideoDemo.lPlay)) {
                System.out.println("停止预览成功");
            }
        }
    }

    //播放库抓图
    public static void getPicbyPlayCtrl() {

        IntByReference pWidth = new IntByReference(0);
        IntByReference pHieght = new IntByReference(0);
        boolean bFlag = playControl.PlayM4_GetPictureSize(m_lPort.getValue(), pWidth, pHieght);
        if (!bFlag) {
            System.out.println("获取失败：" + playControl.PlayM4_GetLastError(m_lPort.getValue()));
            return;
        }
        System.out.println(pWidth.getValue());
        System.out.println(pHieght.getValue());
        IntByReference RealPicSize = new IntByReference(0);
        int picsize = pWidth.getValue() * pHieght.getValue() * 5;
        HCNetSDK.BYTE_ARRAY picByte = new HCNetSDK.BYTE_ARRAY(picsize);
        picByte.write();
        Pointer pByte = picByte.getPointer();
        boolean b_GetPic = playControl.PlayM4_GetJPEG(m_lPort.getValue(), pByte, picsize, RealPicSize);
        if (!b_GetPic) {
            System.out.println("抓图失败：" + playControl.PlayM4_GetLastError(m_lPort.getValue()));
            return;
        }
        picByte.read();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HHmmssSSS");
        String newName = sf.format(new Date());
        FileOutputStream fout = null;
        try {

            fout = new FileOutputStream(System.getProperty("user.dir") + "//pic//" + newName + ".jpg");
            //将字节写入文件
            long offset = 0;
            ByteBuffer buffers = pByte.getByteBuffer(offset, RealPicSize.getValue());
            byte[] bytes = new byte[RealPicSize.getValue()];
            buffers.rewind();
            buffers.get(bytes);
            fout.write(bytes);
            fout.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("抓图成功!");

    }


    /**
     * 按时间回放获取码流数据
     *
     * @param userID
     */

    public void PlayBackBytime(int userID, int iChannelNo) {
        file = new File(System.getProperty("user.dir") + "\\Download\\Videodatabytime.mp4");  //保存回调函数的音频数据

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HCNetSDK.NET_DVR_VOD_PARA net_dvr_vod_para = new HCNetSDK.NET_DVR_VOD_PARA();
        net_dvr_vod_para.dwSize = net_dvr_vod_para.size();
        net_dvr_vod_para.struIDInfo.dwChannel = iChannelNo; //通道号
        net_dvr_vod_para.struBeginTime.dwYear = 2022;
        net_dvr_vod_para.struBeginTime.dwMonth = 4;
        net_dvr_vod_para.struBeginTime.dwDay = 17;
        net_dvr_vod_para.struBeginTime.dwHour = 10;
        net_dvr_vod_para.struBeginTime.dwMinute = 05;
        net_dvr_vod_para.struBeginTime.dwSecond = 00;
        //停止时间
        net_dvr_vod_para.struEndTime.dwYear = 2022;
        net_dvr_vod_para.struEndTime.dwMonth = 4;
        net_dvr_vod_para.struEndTime.dwDay = 17;
        net_dvr_vod_para.struEndTime.dwHour = 10;
        net_dvr_vod_para.struEndTime.dwMinute = 8;
        net_dvr_vod_para.struEndTime.dwSecond = 00;
        net_dvr_vod_para.hWnd = null; // 回放的窗口句柄，若置为空，SDK仍能收到码流数据，但不解码显示
        net_dvr_vod_para.write();

        int iPlayBack = hCNetSDK.NET_DVR_PlayBackByTime_V40(userID, net_dvr_vod_para);
        if (iPlayBack <= -1) {
            System.out.println("按时间回放失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }

        //开启取流        
        if (!hCNetSDK.NET_DVR_PlayBackControl_V40(iPlayBack, HCNetSDK.NET_DVR_PLAYSTART, null, 0, null, new IntByReference(0))) {
            System.out.println("NET_DVR_PLAYSTART失败，错误号：" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }

        //设置回放回调函数
        if (playBackCallBack == null) {
            playBackCallBack = new playDataCallBack();
        }
        boolean bRet = hCNetSDK.NET_DVR_SetPlayDataCallBack_V40(iPlayBack, playBackCallBack, Pointer.NULL);

        //开始计时器
        Playbacktimer = new Timer();//新建定时器
        Playbacktimer.schedule(new PlaybackTask(), 0, 5000);//0秒后开始响应函数

        while (!bEnd) {
            //等待回放结束
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //如果还没有释放回放资源，需要调用NET_DVR_StopPlayBack释放
        if (iPlayBack > 0) {
            hCNetSDK.NET_DVR_StopPlayBack(iPlayBack);
            iPlayBack = -1;
        }

        Playbacktimer.cancel();  //关闭定时器      
        return;
    }


    public static void playBackByfile(int userID, int iChannelNo) {
        file = new File(System.getProperty("user.dir") + "\\Download\\Videodatabyfile.mp4");  //保存回调函数的音频数据

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strFileName = "";
        HCNetSDK.NET_DVR_FILECOND_V40 struFileCond = new HCNetSDK.NET_DVR_FILECOND_V40();
        struFileCond.read();
        struFileCond.lChannel = iChannelNo; //通道号 NVR设备路数小于32路的起始通道号从33开始，依次增加
        struFileCond.byFindType = 0;  //录象文件类型 0=定时录像
        //起始时间
        struFileCond.struStartTime.dwYear = 2022;
        struFileCond.struStartTime.dwMonth = 4;
        struFileCond.struStartTime.dwDay = 17;
        struFileCond.struStartTime.dwHour = 10;
        struFileCond.struStartTime.dwMinute = 00;
        struFileCond.struStartTime.dwSecond = 00;
        //停止时间
        struFileCond.struStopTime.dwYear = 2022;
        struFileCond.struStopTime.dwMonth = 4;
        struFileCond.struStopTime.dwDay = 17;
        struFileCond.struStopTime.dwHour = 10;
        struFileCond.struStopTime.dwMinute = 10;
        struFileCond.struStopTime.dwSecond = 0;
        struFileCond.write();
        int FindFileHandle = hCNetSDK.NET_DVR_FindFile_V40(userID, struFileCond);
        if (FindFileHandle <= -1) {
            System.out.println("查找建立失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
        }

        while (true) {
            HCNetSDK.NET_DVR_FINDDATA_V40 struFindData = new HCNetSDK.NET_DVR_FINDDATA_V40();

            long State = hCNetSDK.NET_DVR_FindNextFile_V40(FindFileHandle, struFindData);
            if (State <= -1) {

                System.out.println("查找失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
                return;

            } else if (State == 1000)  //获取文件信息成功
            {
                struFindData.read();
                try {
                    strFileName = new String(struFindData.sFileName, "UTF-8").trim();
                    System.out.println("文件名称：" + strFileName);
                    System.out.println("文件大小:" + struFindData.dwFileSize);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("获取文件成功");
                break;

            } else if (State == 1001) //未查找到文件
            {
                System.out.println("未查找到文件");
                break;

            } else if (State == 1002) //正在查找请等待
            {
                System.out.println("正在查找，请等待");
                continue;

            } else if (State == 1003) //没有更多的文件，查找结束
            {
                System.out.println("没有更多的文件，查找结束");
                break;

            } else if (State == 1004) //查找文件时异常
            {

                System.out.println("没有更多的文件，查找结束");
                break;

            } else if (State == 1005) //查找文件超时
            {

                System.out.println("没有更多的文件，查找结束");
                break;

            }

        }
        boolean b_CloseHandle = hCNetSDK.NET_DVR_FindClose_V30(FindFileHandle);
        if (!b_CloseHandle) {
            System.out.println("关闭失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }

        int lPlayByFileHandle = hCNetSDK.NET_DVR_PlayBackByName(userID, strFileName, null);
        if (lPlayByFileHandle <= -1) {
            System.out.println("按文件回放失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        IntByReference intP1 = new IntByReference(0);
        IntByReference intInlen = new IntByReference(0);
        boolean b_PlayBackStart = hCNetSDK.NET_DVR_PlayBackControl_V40(lPlayByFileHandle, HCNetSDK.NET_DVR_PLAYSTART, intP1.getPointer(), 4, Pointer.NULL, intInlen);
        if (!b_PlayBackStart) {
            System.out.println("开始播放失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        if (playBackCallBack == null) {
            playBackCallBack = new playDataCallBack();
        }

        boolean bRet = hCNetSDK.NET_DVR_SetPlayDataCallBack_V40(lPlayByFileHandle, playBackCallBack, Pointer.NULL);
        while (true) {
            int Pos = hCNetSDK.NET_DVR_GetDownloadPos(lPlayByFileHandle);
            if (Pos != 100) {
                System.out.println("回放进度:" + Pos);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else {
                break;
            }
        }
        boolean b_Stop = hCNetSDK.NET_DVR_StopPlayBack(lPlayByFileHandle);
        if (!b_Stop) {
            System.out.println("停止回放失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        System.out.println("回放成功");
        return;
    }

    //按文件下载录像(设置转成3GP格式)
    public static void DownloadRecordByFile(int userID, int iChannelNo) {
        bEnd = false;

        String strFileName = "";
        HCNetSDK.NET_DVR_FILECOND_V40 struFileCond = new HCNetSDK.NET_DVR_FILECOND_V40();
        struFileCond.read();
        struFileCond.lChannel = iChannelNo; //通道号 NVR设备路数小于32路的起始通道号从33开始，依次增加
        struFileCond.byFindType = 0;  //录象文件类型 0=定时录像
        //起始时间
        struFileCond.struStartTime.dwYear = 2022;
        struFileCond.struStartTime.dwMonth = 4;
        struFileCond.struStartTime.dwDay = 9;
        struFileCond.struStartTime.dwHour = 0;
        struFileCond.struStartTime.dwMinute = 00;
        struFileCond.struStartTime.dwSecond = 00;
        //停止时间
        struFileCond.struStopTime.dwYear = 2022;
        struFileCond.struStopTime.dwMonth = 4;
        struFileCond.struStopTime.dwDay = 9;
        struFileCond.struStopTime.dwHour = 24;
        struFileCond.struStopTime.dwMinute = 0;
        struFileCond.struStopTime.dwSecond = 0;
        struFileCond.write();

        int FindFileHandle = hCNetSDK.NET_DVR_FindFile_V40(userID, struFileCond);
        if (FindFileHandle <= -1) {
            System.out.println("查找建立失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
        }

        boolean bFind = false;
        while (true) {
            HCNetSDK.NET_DVR_FINDDATA_V40 struFindData = new HCNetSDK.NET_DVR_FINDDATA_V40();

            long State = hCNetSDK.NET_DVR_FindNextFile_V40(FindFileHandle, struFindData);
            if (State <= -1) {
                System.out.println("查找失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
                return;
            } else if (State == 1000)  //获取文件信息成功
            {
                struFindData.read();
                try {
                    strFileName = new String(struFindData.sFileName, "UTF-8").trim();
                    System.out.println("文件名称：" + strFileName);
                    System.out.println("文件大小:" + struFindData.dwFileSize);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("获取文件成功");
                bFind = true;
                break;
            } else if (State == 1001) //未查找到文件
            {
                System.out.println("未查找到文件");
                break;
            } else if (State == 1002) //正在查找请等待
            {
                System.out.println("正在查找，请等待");
                continue;
            } else if (State == 1003) //没有更多的文件，查找结束
            {
                System.out.println("没有更多的文件，查找结束");
                break;
            } else if (State == 1004) //查找文件时异常
            {
                System.out.println("没有更多的文件，查找结束");
                break;
            } else if (State == 1005) //查找文件超时
            {

                System.out.println("没有更多的文件，查找结束");
                break;
            }

        }
        boolean b_CloseHandle = hCNetSDK.NET_DVR_FindClose_V30(FindFileHandle);
        if (!b_CloseHandle) {
            System.out.println("关闭失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }

        if (!bFind) {
            return; //没有查找到文件
        }

        //按文件下载录像
        String SaveDir = ".\\Download\\test.mp4";

        int FileName = hCNetSDK.NET_DVR_GetFileByName(userID, strFileName, SaveDir.getBytes());
        if (FileName <= -1) {
            System.out.println("下载录像失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        //转码3GP命令
//        IntByReference intP=new IntByReference(5);
/*        boolean b_PlayBack=hCNetSDK.NET_DVR_PlayBackControl_V40(FileName,32,intP.getPointer(),4,Pointer.NULL,0);
        if (!b_PlayBack) {
            System.out.println("转封装失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }*/

        IntByReference intP1 = new IntByReference(0);
        IntByReference intInlen = new IntByReference(0);
        boolean b_PlayBackStart = hCNetSDK.NET_DVR_PlayBackControl_V40(FileName, HCNetSDK.NET_DVR_PLAYSTART, intP1.getPointer(), 4, Pointer.NULL, intInlen);
        if (!b_PlayBackStart) {
            System.out.println("开始播放失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }

/*        IntByReference intP=new IntByReference(30*1024);
        IntByReference intInlen1=new IntByReference(0);
        boolean b_PlayBack=hCNetSDK.NET_DVR_PlayBackControl_V40(FileName,24,intP.getPointer(),4, Pointer.NULL,intInlen1);
        if (!b_PlayBack) {
            System.out.println("设置下载速度失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }*/
        while (true) {
            int Pos = hCNetSDK.NET_DVR_GetDownloadPos(FileName);
            if (Pos != 100) {
                System.out.println("下载进度:" + Pos);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else {
                break;
            }
        }
        boolean b_Stop = hCNetSDK.NET_DVR_StopGetFile(FileName);
        if (!b_Stop) {
            System.out.println("停止下载失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        System.out.println("下载成功");
        return;
    }

    //按时间下载录像(不支持转码3GP格式)
    public void DowmloadRecordByTime(int userID, int iChannelNo) {
        bEnd = false;

        HCNetSDK.NET_DVR_PLAYCOND net_dvr_playcond = new HCNetSDK.NET_DVR_PLAYCOND();
        net_dvr_playcond.read();
        net_dvr_playcond.dwChannel = iChannelNo; //通道号 NVR设备路数小于32路的起始通道号从33开始，依次增加

        //开始时间
        net_dvr_playcond.struStartTime.dwYear = 2022;
        net_dvr_playcond.struStartTime.dwMonth = 4;
        net_dvr_playcond.struStartTime.dwDay = 26;
        net_dvr_playcond.struStartTime.dwHour = 11;
        net_dvr_playcond.struStartTime.dwMinute = 00;
        net_dvr_playcond.struStartTime.dwSecond = 00;
        //停止时间
        net_dvr_playcond.struStopTime.dwYear = 2022;
        net_dvr_playcond.struStopTime.dwMonth = 4;
        net_dvr_playcond.struStopTime.dwDay = 26;
        net_dvr_playcond.struStopTime.dwHour = 11;
        net_dvr_playcond.struStopTime.dwMinute = 10;
        net_dvr_playcond.struStopTime.dwSecond = 00;
        net_dvr_playcond.write();

        String sFileName = ".\\Download\\" + System.currentTimeMillis() + ".mp4";
        System.out.println(sFileName);

        m_lLoadHandle = hCNetSDK.NET_DVR_GetFileByTime_V40(userID, sFileName, net_dvr_playcond);
        if (m_lLoadHandle >= 0) {
            if (!hCNetSDK.NET_DVR_PlayBackControl_V40(m_lLoadHandle, HCNetSDK.NET_DVR_PLAYSTART, null, 0, null, new IntByReference(0))) {
                int iErr = hCNetSDK.NET_DVR_GetLastError();
                System.out.println("NET_DVR_PLAYSTART失败，错误号：" + iErr);
                return;
            }
        
/*            IntByReference intP=new IntByReference(5*8*1024);
            IntByReference intInlen=new IntByReference(0);
            boolean b_PlayBack=ClientDemo.hCNetSDK.NET_DVR_PlayBackControl_V40(m_lLoadHandle,24,intP.getPointer(),4,Pointer.NULL,intInlen);
            if (!b_PlayBack) {
                System.out.println("设置下载速度失败，错误码为" + ClientDemo.hCNetSDK.NET_DVR_GetLastError());
                return;
            }*/
            Date nowTime = new Date(System.currentTimeMillis());
            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
            System.out.println("开始下载时间：" + sdFormatter.format(nowTime));
            Downloadtimer = new Timer();//新建定时器
            Downloadtimer.schedule(new DownloadTask(), 0, 5000);//0秒后开始响应函数

            while (!bEnd) {
                //等待下载结束
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            //如果还没有释放下载资源，需要调用NET_DVR_StopGetFile释放
            if (m_lLoadHandle > 0) {
                hCNetSDK.NET_DVR_StopGetFile(m_lLoadHandle);
                m_lLoadHandle = -1;
            }

            Downloadtimer.cancel(); //关闭定时器
        } else {
            System.out.println("按时间下载失败");
            System.out.println("laste error " + hCNetSDK.NET_DVR_GetLastError());
            return;
        }
    }

    /*************************************************
     类:      DownloadTask
     类描述:  下载定时器响应函数
     *************************************************/
    class DownloadTask extends java.util.TimerTask {
        //定时器函数
        @Override
        public void run() {
            System.out.println("定时器触发，下载句柄iPlayBack：" + m_lLoadHandle);

            IntByReference nPos = new IntByReference(0);
            if (!hCNetSDK.NET_DVR_PlayBackControl_V40(m_lLoadHandle, HCNetSDK.NET_DVR_PLAYGETPOS, null, 0, nPos.getPointer(), new IntByReference(0))) {
                bEnd = true;
                System.out.println("NET_DVR_PLAYGETPOS失败，错误号：" + hCNetSDK.NET_DVR_GetLastError());
            }

            if (nPos.getValue() > 100) {
                m_lLoadHandle = -1;
                Downloadtimer.cancel();
                bEnd = true;
                System.out.println("由于网络原因或DVR忙,下载异常终止!");
            }

            if (nPos.getValue() == 100) {
                Date nowTime = new Date(System.currentTimeMillis());
                SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                System.out.println("结束下载时间：" + sdFormatter.format(nowTime));
                m_lLoadHandle = -1;
                Downloadtimer.cancel();
                bEnd = true;
                System.out.println("按时间下载结束!");
            }
        }
    }


    class PlaybackTask extends java.util.TimerTask {
        //定时器函数
        @Override
        public void run() {
            System.out.println("定时器触发，回放句柄iPlayBack：" + iPlayBack);

            IntByReference nPos = new IntByReference(0);
            boolean bret = hCNetSDK.NET_DVR_PlayBackControl_V40(iPlayBack, HCNetSDK.NET_DVR_PLAYGETPOS, null, 0, nPos.getPointer(), new IntByReference(0));
            if (bret) {
                System.out.println("回放进度：" + nPos.getValue());
            } else {
                System.out.println("获取回放进度失败，错误码: " + hCNetSDK.NET_DVR_GetLastError());
                bEnd = true;
            }

            //网络异常导致回放连接断开
            if (nPos.getValue() > 100) {
                //停止回放，释放资源
                hCNetSDK.NET_DVR_StopPlayBack(iPlayBack);
                iPlayBack = -1;
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                bEnd = true;
                System.out.println("由于网络原因或DVR忙,回放异常终止!");
            }

            //回放结束
            if (nPos.getValue() == 100) {
                //停止回放，释放资源
                hCNetSDK.NET_DVR_StopPlayBack(iPlayBack);
                iPlayBack = -1;
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                bEnd = true;
                System.out.println("按时间回放结束");
            }
        }
    }


    //获取IP通道
    public static void getIPChannelInfo(int userID) {
        IntByReference ibrBytesReturned = new IntByReference(0);//获取IP接入配置参数
        HCNetSDK.NET_DVR_IPPARACFG_V40 m_strIpparaCfg = new HCNetSDK.NET_DVR_IPPARACFG_V40();
        m_strIpparaCfg.write();
        //lpIpParaConfig 接收数据的缓冲指针
        Pointer lpIpParaConfig = m_strIpparaCfg.getPointer();
        boolean bRet = hCNetSDK.NET_DVR_GetDVRConfig(userID, HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, lpIpParaConfig, m_strIpparaCfg.size(), ibrBytesReturned);
        m_strIpparaCfg.read();
        System.out.println("起始数字通道号：" + m_strIpparaCfg.dwStartDChan);

        for (int iChannum = 0; iChannum < m_strIpparaCfg.dwDChanNum; iChannum++) {
            int channum = iChannum + m_strIpparaCfg.dwStartDChan;
            m_strIpparaCfg.struStreamMode[iChannum].read();
            if (m_strIpparaCfg.struStreamMode[iChannum].byGetStreamType == 0) {
                m_strIpparaCfg.struStreamMode[iChannum].uGetStream.setType(HCNetSDK.NET_DVR_IPCHANINFO.class);
                m_strIpparaCfg.struStreamMode[iChannum].uGetStream.struChanInfo.read();
                if (m_strIpparaCfg.struStreamMode[iChannum].uGetStream.struChanInfo.byEnable == 1) {
                    System.out.println("IP通道" + channum + "在线");
                } else {
                    System.out.println("IP通道" + channum + "不在线");
                }
            }
        }
    }

    static class fPlayEScallback implements HCNetSDK.FPlayESCallBack {
        public void invoke(int lPreviewHandle, HCNetSDK.NET_DVR_PACKET_INFO_EX pstruPackInfo, Pointer pUser) {
            System.out.println("进入ES裸码流回调");
            System.out.println(pstruPackInfo.dwPacketSize);
            try {
                fileLenth += pstruPackInfo.dwPacketSize;
                fileOutputStream = new FileOutputStream(resultFileName, true);
                //将字节写入文件
                ByteBuffer buffers = pstruPackInfo.pPacketBuffer.getByteBuffer(0, pstruPackInfo.dwPacketSize);
                byte[] bytes = new byte[pstruPackInfo.dwPacketSize];
                buffers.rewind();
                buffers.get(bytes);
                fileOutputStream.write(bytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class playDataCallBack implements HCNetSDK.FPlayDataCallBack {
        public void invoke(int lPlayHandle, int dwDataType, Pointer pBuffer, int dwBufSize, int dwUser) {
            //System.out.println("回放码流回调, 数据类型: " + dwDataType + ", 数据长度:" + dwBufSize);

            //将设备发送过来的回放码流数据写入文件
            long offset = 0;
            ByteBuffer buffers = pBuffer.getByteBuffer(offset, dwBufSize);
            byte[] bytes = new byte[dwBufSize];
            buffers.rewind();
            buffers.get(bytes);
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class FRealDataCallBack implements HCNetSDK.FRealDataCallBack_V30 {
        //预览回调
        public void invoke(int lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser) {
            //System.out.println("码流数据回调, 数据类型: " + dwDataType + ", 数据长度:" + dwBufSize);
            //播放库解码
            switch (dwDataType) {
                case HCNetSDK.NET_DVR_SYSHEAD: //系统头
                    if (!playControl.PlayM4_GetPort(m_lPort)) //获取播放库未使用的通道号
                    {
                        break;
                    }
                    if (dwBufSize > 0) {
                        if (!playControl.PlayM4_SetStreamOpenMode(m_lPort.getValue(), PlayCtrl.STREAME_REALTIME))  //设置实时流播放模式
                        {
                            break;
                        }
                        if (!playControl.PlayM4_OpenStream(m_lPort.getValue(), pBuffer, dwBufSize, 1024 * 1024)) //打开流接口
                        {
                            break;
                        }
                        if (!playControl.PlayM4_Play(m_lPort.getValue(), null)) //播放开始
                        {
                            break;
                        }

                    }
                case HCNetSDK.NET_DVR_STREAMDATA:   //码流数据
                    if ((dwBufSize > 0) && (m_lPort.getValue() != -1)) {
                        if (!playControl.PlayM4_InputData(m_lPort.getValue(), pBuffer, dwBufSize))  //输入流数据
                        {
                            break;
                        }
                    }
            }
        }
    }


}
