package com.ruho.rsk.steps;

import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.utils.NumberParser;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ruho.rsk.steps.LogEventNames.*;

public class StepsFilter {
    public static boolean isRemoveLiquidity(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, LIQUIDITY_REMOVED_V1);
    }

    public static boolean isAddLiquidity(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, LIQUIDITY_ADDED_V1);
    }

    private static boolean isLogEventNamed(RskLogEvent logEvent, String name) {
        if(logEvent.getDecoded() == null) {
            return false;
        }
        if(!StringUtils.hasText(logEvent.getDecoded().getName())) {
            throw new IllegalStateException("decodedData#name is empty");
        }
        return logEvent.getDecoded().getName().equals(name);
    }

    public static boolean isRewardsClaimed(RskLogEvent logEvent) {
        return logEvent.getDecoded().getName().equals(REWARDS_CLAIMED);
    }

    public static boolean isTransfer(RskLogEvent logEvent) {
        return logEvent.getDecoded() != null && TRANSFER.equals(logEvent.getDecoded().getName());
    }

    public static List<RskLogEvent> findTransferEvents(RskItem transaction) {
        return transaction.getLogEvents().stream()
                .filter(StepsFilter::isTransfer)
                .collect(Collectors.toList());
    }

    public static List<RskLogEvent> findTransferEvents(RskItem transaction, TokenContractSpecs token) {
        return findTransferEvents(transaction).stream()
                .filter(logEvent -> logEvent.getSenderAddress().contains(token.getAddress()))
                .collect(Collectors.toList());
    }

    public static RskLogEvent findSentEvent(RskItem transaction) {
        return requireLastEvent(transaction, SENT);
    }

    public static RskLogEvent findWithdrawEvent(RskItem transaction) {
        return requireFirstEvent(transaction, WITHDRAW);
    }

    public static RskLogEvent findIssuanceEvent(RskItem transaction) {
        return requireFirstEvent(transaction, ISSUANCE);
    }

    public static RskLogEvent findRewardsClaimedEvent(RskItem transaction) {
        return requireFirstEvent(transaction, REWARDS_CLAIMED);
    }

    public static RskLogEvent findTokensWithdrawnEvent(RskItem transaction) {
        return requireFirstEvent(transaction, TOKENS_WITHDRAWN);
    }

    public static RskLogEvent findLiquidityAddedEvent(RskItem transaction) {
        return requireFirstEvent(transaction, LIQUIDITY_ADDED_V1);
    }

    public static RskLogEvent findLiquidityRemovedEvent(RskItem transaction) {
        return requireFirstEvent(transaction, LIQUIDITY_REMOVED_V1);
    }

    public static RskLogEvent requireFirstEvent(RskItem transaction, String name) {
        return findFirstEvent(transaction, name)
                .orElseThrow(() -> new IllegalStateException("no " + name + " event found for " + transaction.getTransactionHash()));
    }

    public static RskLogEvent requireLastEvent(RskItem transaction, String name) {
        return findLastEvent(transaction, name)
                .orElseThrow(() -> new IllegalStateException("no " + name + " event found for " + transaction.getTransactionHash()));
    }

    public static List<RskLogEvent> findAllEvents(RskItem transaction, String name) {
        Objects.requireNonNull(name, "name can't be null here for transaction hash: " + transaction.getTransactionHash());
        return transaction.getLogEvents().stream()
                .filter(logEvent -> logEvent.getDecoded() != null)
                .filter(logEvent -> name.equals(logEvent.getDecoded().getName()))
                .collect(Collectors.toList());
    }

    public static Optional<RskLogEvent> findFirstEvent(RskItem transaction, String name) {
        return findAllEvents(transaction, name).stream().findFirst();
    }

    public static Optional<RskLogEvent> findLastEvent(RskItem transaction, String name) {
        Objects.requireNonNull(name, "name can't be null here for transaction hash: " + transaction.getTransactionHash());
        return transaction.getLogEvents().stream()
                .filter(logEvent -> logEvent.getDecoded() != null)
                .filter(logEvent -> name.equals(logEvent.getDecoded().getName()))
                .reduce((rskLogEvent, rskLogEvent2) -> rskLogEvent2);
    }

    public static Optional<RskDecodedData.Param> findFirstParam(RskLogEvent event, String paramName) {
        if(event.getDecoded() == null || event.getDecoded().getParams() == null) {
            return Optional.empty();
        } else {
            return event.getDecoded().getParams().stream()
                    .filter(param -> param.getName().equals(paramName))
                    .findFirst();
        }
    }

    public static BigDecimal findAmountParam(RskLogEvent event) {
        return findAmountParam(event, "value");
    }

    public static BigDecimal findAmountParam(RskLogEvent event, String paramName) {
        return findFirstParam(event, paramName)
                .map(param -> NumberParser.numberFrom(param.getValue(), event.getSenderContractDecimals()))
                .orElseThrow(() -> new IllegalStateException("can't find value param in baseTransfer for " + event.getTransactionHash()));
    }

    public static boolean isMinted(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, MINTED);
    }

    public static boolean isBurned(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, BURNED);
    }

    public static boolean isSent(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, SENT);
    }

    public static boolean isExecuted(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, EXECUTED);
    }

    public static boolean isSpotSwap(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, SPOT_SWAP);
    }

    public static boolean isExternalSwap(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, EXTERNAL_SWAP);
    }

    public static boolean isConversionSwap(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, CONVERSION);
    }

    public static List<RskLogEvent> findTransferEventsToMe(RskItem transaction, String ownWallet) {
        return findTransferEvents(transaction).stream()
                .filter(rskLogEvent ->
                                findFirstParam(rskLogEvent, "to")
                                        .filter(param -> ownWallet.equalsIgnoreCase(String.valueOf(param.getValue())))
                                        .isPresent()
                )
                .collect(Collectors.toList());
    }
}
