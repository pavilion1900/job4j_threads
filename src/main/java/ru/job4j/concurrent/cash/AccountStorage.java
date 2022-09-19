package ru.job4j.concurrent.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {

    @GuardedBy("this")
    private final Map<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.getId(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.getId(), account) != null;
    }

    public synchronized boolean delete(int id) {
        return accounts.remove(id) != null;
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        if (getById(fromId).isPresent() && getById(toId).isPresent() && amount > 0) {
            Account accountFrom = getById(fromId).get();
            Account accountTo = getById(toId).get();
            if (accountFrom.getAmount() >= amount && accountTo.getAmount() >= 0) {
                update(new Account(accountFrom.getId(), accountFrom.getAmount() - amount));
                update(new Account(accountTo.getId(), accountTo.getAmount() + amount));
                result = true;
            }
        }
        return result;
    }
}
