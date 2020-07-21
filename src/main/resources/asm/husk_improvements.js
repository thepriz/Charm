function initializeCoreMod() {
    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var IincInsnNode = Java.type('org.objectweb.asm.tree.IincInsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {
        'husk_improvements': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.monster.HuskEntity',
                'methodName': 'func_223334_b', // canMonsterSpawn ?
                'methodDesc': '(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/IWorld;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)Z'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.INVOKEINTERFACE) {
                        var label = new LabelNode();
                        newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "canHuskSpawnInLight", "(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;)Z", ASM.MethodType.STATIC))

                        method.instructions.insert(instruction, newInstructions);
                        method.instructions.remove(instruction);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (HuskImprovements) " + (success ? "Patched HuskEntity" : "Failed to patch HuskEntity"));
                return method;
            }
        }
    }
}