package com.enation.javashop.android.middleware.api

import com.enation.javashop.android.middleware.model.PostCommentModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.*

/**
 * @author LDD
 * @Date   2018/1/16 下午12:10
 * @From   com.enation.javashop.android.middleware.api
 * @Note   会员Api
 */
interface MemberApi {

    /**
     * @author LDD
     * @From   MemberApi
     * @Date   2018/8/13 上午11:05
     * @Note   获取会员信息
     * @param  会员信息
     */
    @GET("members")
    fun memberInfo() :Observable<ResponseBody>

    /**
     * @author LDD
     * @From   MemberApi
     * @Date   2018/8/13 下午8:56
     * @Note   退出登录
     * @param  uid  用户表示ID
     */
    @POST("members/logout")
    fun logout(@Query("uid") uid :String) :Observable<ResponseBody>

    /**
     * @author LDD
     * @From   MemberApi
     * @Date   2018/8/15 下午9:10
     * @Note   更改会员Info
     * @param  nickname 会员信息
     * @param  region   地区
     * @param  sex      性别
     * @param  birthday 生日
     * @param  address  详细地址
     * @param  email    电子邮件
     * @param  tel      电话
     * @param  face     头像
     */
    @PUT("members")
    fun editMemberInfo(@Query("nickname") nickname:String,
                       @Query("region") region :Int?,
                       @Query("sex") sex :Int,
                       @Query("birthday")birthday :Long,
                       @Query("address") address :String,
                       @Query("email") email :String,
                       @Query("tel") tel :String,
                       @Query("face") face :String) : Observable<ResponseBody>

    /**
     * @author LDD
     * @From   MemberApi
     * @Date   2018/8/15 下午9:12
     * @Note   获取会员 计数信息 订单数 等等
     */
    @GET("members/statistics")
    fun getMemberOther() : Observable<ResponseBody>

    /** ==============================地址=============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询当前会员的某个地址
     * @param   id    要查询的地址id
     */
    @GET("members/address/{id}")
        fun getAddressDetail(@Path("id") id :Int):Observable<Result<ResponseBody>>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   删除会员地址
     * @param   id    要删除的会员地址id
     */
    @DELETE("members/address/{id}")
    fun deleteAddress(@Path("id") id :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   修改会员地址
     * @param   name    收货人姓名
     * @param   addr    详细地址
     * @param   tel    联系电话(一般指座机)
     * @param   mobile    手机号码
     * @param   defAddr    是否为默认收货地址,1为默认
     * @param   shipAddressName    地址别名
     * @param   region    地区
     * @param   id    主键
     */
    @PUT("members/address/{id}")
    fun editAddress(@Path("id") id :Int,@Query("name") name :String,@Query("addr") addr :String,
                    @Query("tel") tel :String,@Query("mobile") mobile :String,@Query("def_addr") defAddr :Int,
                    @Query("ship_address_name") shipAddressName :String,@Query("region") region :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   添加会员地址
     * @param   name    收货人姓名
     * @param   addr    详细地址
     * @param   tel    联系电话(一般指座机)
     * @param   mobile    手机号码
     * @param   defAddr    是否为默认收货地址,1为默认
     * @param   shipAddressName    地址别名
     * @param   region    地区
     */
    @POST("members/address")
    fun addAddress(@Query("name") name :String,@Query("addr") addr :String,@Query("tel") tel :String,
                   @Query("mobile") mobile :String,@Query("def_addr") defAddr :Int,
                   @Query("ship_address_name") shipAddressName :String,@Query("region") region :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   设置地址为默认
     * @param   id    主键
     */
    @PUT("members/address/{id}/default")
    fun setAddressDefault(@Path("id") id :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询当前会员地址列表
     */
    @GET("members/addresses")
    fun getAddressList():Observable<ResponseBody>


    /** ============================== 会员商品收藏表相关API =============================== */


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   删除会员商品收藏
     * @param   goodsId    商品id
     */
    @DELETE("members/collection/goods/{goods_id}")
    fun deleteCollection(@Path("goods_id") goodsId :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   添加会员商品收藏
     * @param   goodsId    商品id
     */
    @POST("members/collection/goods")
    fun addCollection(@Query("goods_id") goodsId :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询会员商品收藏列表
     * @param   pageNo    页码
     * @param   pageSize    每页显示数量
     */
    @GET("members/collection/goods")
    fun getCollectionList(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE):Observable<ResponseBody>

    /** ============================== 会员收藏店铺表相关API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   删除会员收藏店铺
     * @param   shopId    要删除的店铺id
     */
    @DELETE("members/collection/shop/{shop_id}")
    fun deleteCollectionShop(@Path("shop_id") shopId :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   添加会员收藏店铺
     * @param   shopId    店铺id
     */
    @POST("members/collection/shop")
    fun addCollectionShop(@Query("shop_id") shopId :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询会员收藏店铺列表
     * @param   pageNo    页码
     * @param   pageSize    每页显示数量
     */
    @GET("members/collection/shops")
    fun getCollectionShopList(@Query("page_no") pageNo :Int,
                              @Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE):Observable<ResponseBody>


    /** ============================== 评论相关API =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   提交评论
     * @param   comment    comment
     */
    @POST("members/comments")
    fun addComment(@Body comment : PostCommentModel):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-27 17:40:12
     * @Note   查询某商品的评论
     * @param   content    评论内容
     * @param   goodsName    商品名称
     * @param   grade    好中差评
     * @param   haveImage    是否有图
     * @param   keyword    模糊查询的关键字
     * @param   memberId    会员id
     * @param   memberName    会员名称
     * @param   replyStatus    回复状态
     * @param   pageNo    页码
     * @param   pageSize    每页显示数量
     * @param   goodsId    商品ID
     */
    @GET("members/comments/goods/{goods_id}")
    fun getComments(@Path("goods_id") goodsId :Int,@Query("grade") grade :String?,
             @Query("have_image") haveImage :Boolean?,@Query("page_no") pageNo :Int,
             @Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE):Observable<ResponseBody>



    /** ============================== 会员信任登录API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   获取绑定列表API
     */
    @GET("account-binder/list")
    fun getBinderList():Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   登录绑定openid
     * @param   uuid    客户端唯一标识
     */
    @POST("account-binder/login/{uuid}")
    fun openidBind(@Path("uuid") uuid :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   注册绑定openid
     * @param   uuid    客户端唯一标识
     */
    @POST("account-binder/register/{uuid}")
    fun registerBind(@Path("uuid") uuid :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   会员解绑操作
     * @param   type    登录方式:QQ,微博,微信,支付宝
     */
    @POST("account-binder/pc/{type}")
    fun unbind(@Path("type") type :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   发起账号绑定
     * @param   type    登录方式:QQ,微博,微信,支付宝
     */
    @GET("account-binder/pc/{type}")
    fun initiate(@Path("type") type :String):Observable<ResponseBody>


    /** ============================== 会员优惠券相关API =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   用户领取优惠卷
     * @param   couponId    优惠券id
     */
    @POST("members/coupon/{coupon_id}/receive")
    fun receiveCoupon(@Path("coupon_id") couponId :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询我的优惠券列表
     * @param   status    优惠券状态 0为全部，1为未使用，2为已使用，3为已过期
     * @param   pageNo    页数
     * @param   pageSize    条数
     */
    @GET("members/coupon")
    fun getCouponListByAll(@Query("status") status :Int,@Query("page_no") pageNo :Int,
                           @Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE):Observable<ResponseBody>


    /** ============================== 会员积分相关API =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询会员积分列表
     * @param   pageNo    页码
     * @param   pageSize    每页显示数量
     */
    @GET("members/points")
    fun getPointList(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询当前会员的积分
     */
    @GET("members/points/current")
    fun getPoint():Observable<ResponseBody>


    /** ============================== 会员发票相关API =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   修改会员增值税普通发票
     * @param   receiptTitle    发票抬头
     * @param   receiptContent    发票内容
     * @param   taxNo    发票税号
     * @param   id    主键
     */
    @PUT("members/receipt/{id}/ordinary")
    fun editReceipt(@Path("id") id :Int,@Query("receipt_title") receiptTitle :String,@Query("receipt_content") receiptContent :String,@Query("tax_no") taxNo :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   删除会员发票
     * @param   id    要删除的会员发票主键
     */
    @DELETE("members/receipt/{id}")
    fun deleteReceipt(@Path("id") id :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   添加会员增值税普通发票
     * @param   receiptTitle    发票抬头
     * @param   receiptContent    发票内容
     * @param   taxNo    发票税号
     */
    @POST("members/receipt/ordinary")
    fun addReceipt(@Query("receipt_title") receiptTitle :String,
                   @Query("receipt_content") receiptContent :String,
                   @Query("tax_no") taxNo :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询当前会员发票列表
     */
    @GET("members/receipt")
    fun getReceiptList():Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   设置会员发票为默认
     * @param   receiptType    枚举，ELECTRO:电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票
     * @param   id    会员发票主键,如果选择个人则设置此参数为0
     */
    @PUT("members/receipt/{id}/default")
    fun defaultReceipt(@Path("id") id :Int,@Query("receipt_type") receiptType :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   根据订单sn查询订单发票信息
     * @param   orderSn    订单sn
     */
    @GET("members/receipt/{order_sn}")
    fun getReceipt(@Path("order_sn") orderSn :String):Observable<ResponseBody>



    /** ============================== 会员安全API =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   手机号码绑定API
     * @param   smsCode    手机验证码
     * @param   mobile    手机号
     */
    @PUT("members/security/bind/{mobile}")
    fun bindMobile(@Path("mobile") mobile :String,@Query("sms_code") smsCode :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   验证换绑验证验证码
     * @param   smsCode    验证码
     */
    @GET("members/security/exchange-bind")
    fun checkExchangeBindCode(@Query("sms_code") smsCode :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   发送绑定手机验证码
     * @param   uuid    uuid客户端的唯一标识
     * @param   captcha    图片验证码
     * @param   mobile    手机号码
     */
    @POST("members/security/bind/send/{mobile}")
    fun sendBindSmsCode(@Path("mobile") mobile :String,@Query("uuid") uuid :String,
                        @Query("captcha") captcha :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   验证修改密码验证码
     * @param   smsCode    验证码
     */
    @GET("members/security/password")
    fun checkUpdatePwdCode(@Query("sms_code") smsCode :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   修改密码
     * @param   uuid    uuid客户端的唯一标识
     * @param   captcha    图片验证码
     * @param   password    密码
     */
    @PUT("members/security/password")
    fun updatePassword(@Query("uuid") uuid :String,@Query("captcha") captcha :String,
                       @Query("password") password :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   手机号码换绑API
     * @param   smsCode    手机验证码
     * @param   mobile    手机号
     */
    @PUT("members/security/exchange-bind/{mobile}")
    fun exchangeBindMobile(@Path("mobile") mobile :String,@Query("sms_code") smsCode :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   发送手机验证验证码
     * @param   uuid    uuid客户端的唯一标识
     * @param   captcha    图片验证码
     */
    @POST("members/security/send")
    fun sendValSmsCode(@Query("uuid") uuid :String,@Query("captcha") captcha :String):Observable<ResponseBody>


}