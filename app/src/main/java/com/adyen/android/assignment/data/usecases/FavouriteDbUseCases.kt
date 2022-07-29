package com.adyen.android.assignment.data.usecases

data class FavouriteDbUseCases(
    val getFavouriteUseCase: GetFavourite,
    val deleteFavourite: DeleteFavourite,
    val insertFavourite: InsertFavourite
)
