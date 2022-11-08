package com.adyen.android.assignment.domain.usecases

data class FavouriteDbUseCases(
    val getFavouriteUseCase: GetFavourite,
    val deleteFavourite: DeleteFavourite,
    val insertFavourite: InsertFavourite
)
