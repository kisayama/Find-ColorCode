package com.example.findcolorcode.data


//お気に入りの色に対して実行できる操作を定義したメニューリスト
val favoriteColorActionsMenuList = listOf(
    "左パレットに移動","右パレットに移動","カラーコードをコピー","変更","削除"
)
//お気に入りの色を並び替える条件を定義したメニューリスト
val favoriteColorScreenSortMenuList = listOf(
    "日付:新しい順","日付：古い順"
)
//色調整ボタンの変更単位を定義したメニューリスト
val adjustValueChangeMenuList = listOf(
    1,50,100,255
)

//getColorSchemeで使用するカラーパレット作成モードリスト
val colorSchemeModeList = listOf(
    Pair("調和的、穏やか", "analogic"),
    Pair("コントラスト", "complement"),
    Pair("バランス", "analogic-complement"),
    Pair("鮮やか", "triad"),
    Pair("とても鮮やか", "quad"),
    Pair("モノクロ", "monochrome"),
    Pair("モノクロライト", "monochrome-light"),
    Pair("モノクロダーク", "monochrome-dark"),
)