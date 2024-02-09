package movil.siafeson.simgolp.interfaces

import androidx.appcompat.widget.Toolbar

interface iToolBar  {
    fun toolbarToLoad(toolbar: Toolbar?, titulo: String = "")
    fun enableHomeDisplay(value: Boolean)
    fun titulo(subtitulo: String)
    fun subTitulo(subtitulo: String)
}