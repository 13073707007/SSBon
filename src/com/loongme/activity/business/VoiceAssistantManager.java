package com.loongme.activity.business;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ListView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.loongme.activity.R;
import com.loongme.activity.bean.MoreMenuItem;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-3 下午2:35:02 类说明:语音助手管理类
 */
public class VoiceAssistantManager {

	private static final String[] CONTENT = new String[] { "最新资讯", "笑话", "叫快递", "定外卖", "找工作", "天气", "打车", "美食", "健身",
			"掌上医院", "心理咨询", "医生在线", "专家预约", "电话医生", "家庭医生", "陪诊服务", "药品", "育儿经", "减肥", "掌上医院", "开锁", "家装维修", "搬家",
			"空调维修", "超市到家", "家居收纳", "保洁清洗", "保姆月嫂", "叫快递", "管道疏通", "天气", "笑话", "新闻资讯", "酒店", "机票", "火车票", "打车", "定外卖",
			"鲜果速递", "租房子", "美食", "电影", "购物", "摄影", "美容", "美甲", "国内游", "出国游", "自驾游", "萌宠", "游戏控", "炫舞", "飙歌", "演唱会",
			"二次元", "找工作", "读书", "交友", "爱冒险", "爱户外" };
	private static final int[] ICONS = new int[] { R.drawable.icon_zxzx, R.drawable.icon_xh, R.drawable.icon_jkd,
			R.drawable.icon_dwm, R.drawable.icon_zgz, R.drawable.icon_tq, R.drawable.icon_dc, R.drawable.icon_ms,
			R.drawable.icon_js, R.drawable.icon_zsyy, R.drawable.icon_xlzx, R.drawable.icon_yszx, R.drawable.icon_zjyy,
			R.drawable.icon_dhys, R.drawable.icon_jtys, R.drawable.icon_pzfw, R.drawable.icon_yp, R.drawable.icon_yej,
			R.drawable.icon_jf, R.drawable.icon_zsyy, R.drawable.icon_ks, R.drawable.icon_jzwx, R.drawable.icon_bj,
			R.drawable.icon_ktwx, R.drawable.icon_csdj, R.drawable.icon_jjsn, R.drawable.icon_bjqs,
			R.drawable.icon_bmys, R.drawable.icon_jkd, R.drawable.icon_gdst, R.drawable.icon_tq, R.drawable.icon_xh,
			R.drawable.icon_xw, R.drawable.icon_jd, R.drawable.icon_jp, R.drawable.icon_hcp, R.drawable.icon_dc,
			R.drawable.icon_dwm, R.drawable.icon_xgsd, R.drawable.icon_zfz, R.drawable.icon_ms, R.drawable.icon_dy,
			R.drawable.icon_gw, R.drawable.icon_sy, R.drawable.icon_mr, R.drawable.icon_mj, R.drawable.icon_gny,
			R.drawable.icon_cgy, R.drawable.icon_zjy, R.drawable.icon_mc, R.drawable.icon_yxk, R.drawable.icon_xw,
			R.drawable.icon_bg, R.drawable.icon_ych, R.drawable.icon_ecy, R.drawable.icon_zgz, R.drawable.icon_ds,
			R.drawable.icon_jy, R.drawable.icon_amx, R.drawable.icon_ahw };
	// 以后定义成常量 便于维护
	private static final String[] URLS = new String[] {
			"http://toutiao.com/sem/keyword/%E6%96%B0%E9%97%BB%E5%92%A8%E8%AF%A2/?c=e7bb8fe9aa8ce8af8d2de58897e8a1a82de5ae89e58d933231&a=e696b0e997bbe8b584e8aeaf3c303131313e&k=e696b0e997bbe592a8e8afa2&u=3138373438303830&sc=6261696475",
			"http://www.haha365.com/joke",
			"http://bj.58.com/kuaidi",
			"http://i.meituan.com/s/beijing-%E5%A4%96%E5%8D%96",
			"http://bj.58.com/job.shtml",
			"https://m.baidu.com/s?tn=bmbadr&pu=sz%401320_480%2Cosname%40baidubrowser%2Ccua%40_a-qi4uq-igBNE6lI5me6NNy2I_UCv8NzuDp8eLHA%2Ccut%40pfX8OYaS2i41aXiDyuvLCf41mjQm5BqAC%2Cctv%402%2Ccfrom%401000575h%2Ccen%40cuid_cua_cut%2Ccsrc%40app_box_txt&from=1000575h&word=%E5%A4%A9%E6%B0%94",
			"https://partners.uber.com.cn/ob/signup",
			"http://i.meituan.com/beijing?cid=1&stid_b=1&cateType=poi",
			"http://3g.xywy.com/jianfei/",
			"http://bj.58.com/mianbumeir/?refrom=m58",
			"http://3g.club.xywy.com/familyDoctor/list/subject?id=321",
			"http://m.xywy.com/home/self_help/zz.html?fromurl=wk_1",
			"http://3g.zhuanjia.xywy.com",
			"http://3g.zhuanjia.xywy.com/dhyslist.htm?fromurl=wk_2",
			"http://3g.club.xywy.com/familyDoctor/list/subject?id=766&fromurl=3gbanner",
			"http://m.yishengshi.xywy.com",
			"http://3g.yao.xywy.com/?fromurl=wk_1",
			"http://3g.xywy.com/baby?fromurl=Mindex-2",
			"http://3g.xywy.com/jianfei",
			"http://m.xywy.com/home/hospital/index.html",
			"http://bj.58.com/shutong",
			"http://bj.58.com/shutong",
			"http://bj.58.com/shutong",
			"http://bj.58.com/kongtiao",
			"http://bj.58.com/shop.shtml",
			"http://m.58.com/bj/kaisuo",
			"http://bj.58.com/baojie",
			"http://bj.58.com/baomu",
			"http://bj.58.com/kuaidi",
			"http://bj.58.com/shutong",
			"https://m.baidu.com/s?tn=bmbadr&pu=sz%401320_480%2Cosname%40baidubrowser%2Ccua%40_a-qi4uq-igBNE6lI5me6NNy2I_UCv8NzuDp8eLHA%2Ccut%40pfX8OYaS2i41aXiDyuvLCf41mjQm5BqAC%2Cctv%402%2Ccfrom%401000575h%2Ccen%40cuid_cua_cut%2Ccsrc%40app_box_txt&from=1000575h&word=%E5%A4%A9%E6%B0%94",
			"http://www.haha365.com/joke",
			"http://toutiao.com/sem/keyword/%E6%96%B0%E9%97%BB%E5%92%A8%E8%AF%A2/?c=e7bb8fe9aa8ce8af8d2de58897e8a1a82de5ae89e58d933231&a=e696b0e997bbe8b584e8aeaf3c303131313e&k=e696b0e997bbe592a8e8afa2&u=3138373438303830&sc=6261696475",
			"http://www.ctrip.com/?utm_source=baidu&utm_medium=cpc&utm_campaign=baidu81&campaign=CHNbaidu81&adid=index&gclid=&isctrip=T",
			"http://huoche.tuniu.com/?p=1044&utm_source=baidu&utm_medium=cpc&utm_campaign=SE&fc=u10292972.k22661863794.a7600376269.pb",
			"http://kuai.baidu.com/webapp/train/index.html?us=tr_wise_tit&ala_adapt=iphone",
			"https://partners.uber.com.cn/ob/signup?utm_source=baidu-nonbrand&utm_campaign=search-baidu_37_91_cn-beijing_d_mob_acq_cpc_cn-cn_%B4%F2%B3%B5_nat23142_{creative_id}_{adgroup_id}_{match_type}",
			"http://i.meituan.com/s/beijing-%E5%A4%96%E5%8D%96", "http://www.cuixianyuan.com/wap",
			"http://m.58.com/bj/zufang", "http://i.meituan.com/beijing?cid=1&stid_b=1&cateType=poi",
			"http://i.meituan.com/beijing?cid=99&stid_b=1&cateType=poi",
			"http://i.meituan.com/beijing?cid=4&stid_b=1&cateType=deal", "http://m.fengniao.com",
			"http://m.58.com/bj/mianbumeir/?PGTID=0d2070f9-0000-1dd8-57d4-1247d745ecfd&ClickID=2",
			"http://bj.58.com/mrmeijia/?refrom=m58", "http://m.tuniu.com/bj/domestic", "http://m.tuniu.com/bj/abroad",
			"http://m.tuniu.com/bj/drive", "http://bj.58.com/pets.shtml",
			"http://weibo.com/chinaplayers?c=spr_qdhz_bd_baidusmt_weibo_s&nick=%E6%B8%B8%E6%88%8F%E6%8E%A7&is_hot=1",
			"http://wap.juzishu.com.cn/chengnian/Adultdance", "http://wap.juzishu.com.cn/chengnian/Adultvocalmusic",
			"http://www.228.com.cn/?type=1&cityjx=bj&typeajx=yanchanghui&source=baidu",
			"http://comic.qq.com/zt2012/goodgirl", "http://bj.58.com/job.shtml", "https://m.douban.com/book",
			"http://jiaoyou.58.com/bj", "http://www.zgfun.com/forum.php", "http://m.8264.com/bbs" };

	public static VoiceAssistantManager instance;
	private Context mContext;

	private VoiceAssistantManager(Context context) {
		super();
		this.mContext = context;
	}

	public static VoiceAssistantManager getInstance(Context context) {
		if (instance == null) {
			instance = new VoiceAssistantManager(context);
		}
		return instance;
	}

	/**
	 * 根据viewpager页码得到菜单项
	 * 
	 * @param position
	 * @return
	 */
	public List<MoreMenuItem> getDatasWithPostion(int position) {
		List<MoreMenuItem> datas = new ArrayList<MoreMenuItem>();
		for (int i = position * 10; i < position * 10 + 10; i++) {
			MoreMenuItem item = new MoreMenuItem(CONTENT[i], ICONS[i], URLS[i]);
			datas.add(item);
		}
		return datas;
	}

	/**
	 * ***********************小帮的业务逻辑*******************************************
	 */

	public String dealWelcomeMsg(String msg) {
		String str = msg.split("ROBOT_OUTPUT")[1].split("SERIAL_NO")[0].replace("{", "").replace("}", "").trim();
		return str;
	}

	/**
	 * 初始化语音听写
	 * 
	 * @param speechRecognizer
	 */
	public void initSpeechToText(RecognizerDialog mIat) {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "result");
		// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置语言区域
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, "1500");
		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		// mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		// mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
		// Environment.getExternalStorageDirectory() + "/msc/iat.wav");
	}

	/**
	 * 初始化语音合成
	 * 
	 * @param speechRecognizer
	 */
	public void initTextToSpeech(SpeechSynthesizer mTts) {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置在线合成发音人
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		// 设置合成语速
		mTts.setParameter(SpeechConstant.SPEED, "50");
		// 设置合成音调
		mTts.setParameter(SpeechConstant.PITCH, "50");
		// 设置合成音量
		mTts.setParameter(SpeechConstant.VOLUME, "50");
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		// mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		// mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
		// Environment.getExternalStorageDirectory() + "/msc/tts.wav");
	}

	/**
	 * 展示H5的页面
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public WebView getH5WebView(Context context) {
		WebView webView = new WebView(context);
		webView.setLayoutParams(new ListView.LayoutParams(LayoutParams.MATCH_PARENT, UIUtil.dip2px(context, 250)));
		webView.setPadding(5, 0, 5, 10);
		webView.getSettings().setJavaScriptEnabled(true);
		return webView;
	}

}
