package ru.mail.krivonos.al.service.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.AuditItemRepository;
import ru.mail.krivonos.al.repository.model.AuditItem;
import ru.mail.krivonos.al.repository.model.AuditItemActionEnum;
import ru.mail.krivonos.al.repository.model.Item;

import java.sql.Connection;
import java.util.Date;

@Aspect
@Component("itemStatusAspect")
public class ItemAuditAspect {

    private AuditItemRepository auditItemRepository;

    @Autowired
    public ItemAuditAspect(AuditItemRepository auditItemRepository) {
        this.auditItemRepository = auditItemRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(ItemAuditAspect.class);

    @Pointcut("execution(* ru.mail.krivonos.al.repository.ItemRepository.add(..))")
    public void callAtAddMethod() {
    }

    @Pointcut("execution(* ru.mail.krivonos.al.repository.ItemRepository.update(..))")
    public void callAtUpdateMethod() {
    }

    @AfterReturning(value = "callAtAddMethod()", returning = "item")
    public void afterReturningFromAddMethod(JoinPoint joinPoint, Item item) {
        Object[] args = joinPoint.getArgs();
        Connection connection = (Connection) args[0];
        AuditItem auditItem = new AuditItem();
        auditItem.setAction(AuditItemActionEnum.CREATED);
        auditItem.setDate(new Date());
        auditItem.setItemID(item.getId());
        AuditItem addedAuditItem = auditItemRepository.add(connection, auditItem);
        logger.info(addedAuditItem.toString());
    }

    @AfterReturning(value = "callAtUpdateMethod()", returning = "rowsUpdated")
    public void afterReturningFromUpdateMethod(JoinPoint joinPoint, int rowsUpdated) {
        Object[] args = joinPoint.getArgs();
        Connection connection = (Connection) args[0];
        AuditItem auditItem = new AuditItem();
        auditItem.setAction(AuditItemActionEnum.UPDATED);
        auditItem.setDate(new Date());
        Long itemID = (Long) args[1];
        auditItem.setItemID(itemID);
        AuditItem addedAuditItem = auditItemRepository.add(connection, auditItem);
        logger.info(addedAuditItem.toString());
    }
}
