package com.auten.domain.softwareengineering

import com.auten.service.softwareengineering.ValidateService
import com.main.softwareengineering.accesses
import com.main.softwareengineering.users
import kotlinx.cli.*
import java.lang.Exception
import java.time.LocalDate
import kotlin.system.exitProcess

class Params(private val args: Array<String>) {
    private val isHelp: Boolean
    private val parser = ArgParser("example")
    private val login by parser.option(ArgType.String, shortName = "login").default(" ")
    private val pass by parser.option(ArgType.String, shortName = "pass").default(" ")
    private val res by parser.option(ArgType.String, shortName = "res").default(" ")
    private val role by parser.option(ArgType.String, shortName = "role").default(" ")
    private val dss by parser.option(ArgType.String, shortName = "ds").default(" ")
    private val dee by parser.option(ArgType.String, shortName = "de").default(" ")
    private val voll by parser.option(ArgType.String, shortName = "vol").default(" ")
    private var ds: LocalDate?
    private var de: LocalDate?
    private var vol: Int?

    private val validService = ValidateService(users, accesses)

    init {
        try {
            parser.parse(args)
        } catch (e: Exception) {
            exitProcess(1)
        }
        isHelp = args.isNullOrEmpty() or (args.size == 1)
        if (validService.isDateValid(dss))
            ds = LocalDate.parse(dss)
        else
            ds = null
        if (validService.isDateValid(dee))
            de = LocalDate.parse(dee)
        else
            de = null
        try {
            vol = voll.toInt()
        } catch (e: NumberFormatException) {
            vol = null
        }
    }

    fun accounting() {
        if (args.size != 14)
            return
        else {
            when {
                ds == null -> exitProcess(7)
                de == null -> exitProcess(7)
                vol == null -> exitProcess(7)
            }
        }
    }


    fun avtorization() {
        if (args.size < 4)
            exitProcess(1)
        else if (args.size == 4) {
            val us: User? = validService.findUser(login)
            when {
                isHelp -> exitProcess(1)

                !validService.isLoginValid(login) -> exitProcess(2)

                us == null -> exitProcess(3)

                !validService.isPassCorrect(us, pass) -> exitProcess(4)

                else -> exitProcess(0)
            }
        }
    }

    fun autentification() {
        if (args.size != 8)
            return
        else {
            when {
                !Roles.isRoleExist(role) -> exitProcess(5)

                !validService.isUserHasRole(login, Roles.valueOf(role), res) -> exitProcess(6)

                else -> exitProcess(0)
            }
        }
    }

}
