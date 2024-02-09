package movil.siafeson.simgolp.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import movil.siafeson.simgolp.interfaces.iToolBar

open class ToolBarActivity : AppCompatActivity(), iToolBar {

    protected var _toolbar: Toolbar? = null

    override fun toolbarToLoad(toolbar: Toolbar?, titulo: String) {
        _toolbar = toolbar
        _toolbar?.let {
            setSupportActionBar(_toolbar)
            if(!titulo.isEmpty()) setTitle(titulo)
        }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

    override fun titulo(subtitulo: String){
        supportActionBar?.setTitle(subtitulo)
    }

    override fun subTitulo(subtitulo: String){
        supportActionBar?.setSubtitle(subtitulo)
    }

}