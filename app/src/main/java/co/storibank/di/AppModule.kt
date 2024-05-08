package co.storibank.di

import co.storibank.data.repository.BankRepositoryImpl
import co.storibank.data.repository.UserRepositoryImpl
import co.storibank.domain.repository.BankRepository
import co.storibank.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindBankRepository(impl: BankRepositoryImpl): BankRepository
}
