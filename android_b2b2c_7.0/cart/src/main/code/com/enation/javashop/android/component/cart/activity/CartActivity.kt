package com.enation.javashop.android.component.cart.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.enation.javashop.android.component.cart.R
import com.enation.javashop.android.component.cart.fragment.CartFragment
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.utils.base.tool.BaseToolActivity

/**
 * @author LDD
 * @Date   2018/2/24 下午4:29
 * @From   com.enation.javashop.android.component.cart.activity
 * @Note   cartFragment的Activity载体
 */
@Router(path = "/cart/main")
class CartActivity : BaseToolActivity() {

    private lateinit var fragment:CartFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        setContentView(R.layout.cart_act_lay)
        fragment = CartFragment()
        supportFragmentManager?.beginTransaction()?.add(R.id.fragment,fragment)?.addToBackStack(null)?.commit()
    }

}
